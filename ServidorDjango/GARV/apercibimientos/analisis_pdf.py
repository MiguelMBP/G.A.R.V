import csv
import datetime
import re
from collections import Counter

import tabula

from .models import Apercibimiento, AsignaturasEspeciales

literal_fecha_hasta = 'Fecha hasta: '
literal_fecha_desde = 'Fecha desde: '
literal_anno_academico = 'Año académico: '
literal_curso = 'Curso: '
literal_unidad = 'Unidad: '
literal_materia = 'Materia: '
literal_fin_materia = 'Total días período seleccionado: '
expresion_alumno = "^[a-zA-zÀ-ÿ ]+[,][a-zA-zÀ-ÿ ]+[0-9]{1,2}[:][0-9]{2}[ ][0-9]{1,3}[,]{0,1}[0-9]{0,2}[%][ ][0-9]{1,2}[:][0-9]{2}[ ][0-9]{1,3}[,]{0,1}[0-9]{0,2}[%][ ][0-9]{1,2}[:][0-9]{2}"
expresion_nombre_alumno = '^[a-zA-zÀ-ÿ ]+[,][a-zA-zÀ-ÿ ]+'
expresion_horas_asignatura = '[0-9]{1,2}[:][0-9]{2}'
expresion_porcentaje_asignatura = '[0-9]{1,3}[,]{0,1}[0-9]{0,2}[%]'

RUTA_CSV = '/var/www/output.csv'


def pdf_to_csv(filepath):
    tabula.convert_into(filepath, RUTA_CSV, pages="all", output_format="csv", guess=False, stream=True)
    leer_csv()



def leer_csv():
    cabecera = get_cabecera()
    if comprobar_cabecera(cabecera):
        repetidas = contar_asignaturas()
        get_asignaturas(cabecera, repetidas)


def get_cabecera():
    anno = 'nada'
    curso = 'nada'
    unidad = 'nada'
    fecha_inicio = 'nada'
    fecha_fin = 'nada'

    with open(RUTA_CSV, 'r', encoding='utf-8') as csvFile:
        reader = csv.reader(csvFile)

        for row in reader:
            line = row[0]
            if anno == 'nada':
                pos = line.find(literal_anno_academico)
                if pos != -1:
                    anno = line[pos + len(literal_anno_academico):pos + len(literal_anno_academico) + 4]

            if unidad == 'nada':
                pos = line.find(literal_unidad)
                if pos != -1:
                    unidad = line[pos + len(literal_unidad):len(line)]

            if curso == 'nada':
                pos = line.find(literal_curso)
                if pos != -1:
                    pos_fin = line.find(literal_unidad)
                    curso = line[pos + len(literal_curso):pos_fin]

            if fecha_inicio == 'nada':
                pos = line.find(literal_fecha_desde)
                if pos != -1:
                    fecha_temp = line[pos + len(literal_fecha_desde):pos + len(literal_fecha_desde) + 10]
                    fecha_temp = fecha_temp.split('/')
                    fecha_inicio = datetime.date(int(fecha_temp[2]), int(fecha_temp[1]), int(fecha_temp[0]))

            if fecha_fin == 'nada':
                pos = line.find(literal_fecha_hasta)
                if pos != -1:
                    fecha_temp = line[len(literal_fecha_hasta) + pos:len(literal_fecha_hasta) + pos + 10]

                    fecha_temp = fecha_temp.split('/')
                    fecha_fin = datetime.date(int(fecha_temp[2]), int(fecha_temp[1]), int(fecha_temp[0]))

    cabecera = [anno, curso.replace('o', 'º', 1), unidad.replace('o', 'º', 1), fecha_inicio, fecha_fin]

    csvFile.close()

    return cabecera


def contar_asignaturas():
    with open(RUTA_CSV, 'r', encoding='utf-8') as csvFile:
        reader = csv.reader(csvFile)
        materias = []
        for row in reader:
            line = row[0]
            pos = line.find(literal_materia)
            if pos != -1:
                pos_fin = line.find(literal_fin_materia)
                materia = line[pos + len(literal_materia): pos_fin]
                materias.append(materia)
    contador = Counter(materias)
    repetidas = [k for k, v in contador.items() if v > 1]
    global contRepetida
    contRepetida = [0] * (len(repetidas) + 1)
    return repetidas


def comprobar_cabecera(cabecera):
    if cabecera[0] == 'nada'  or cabecera[1] == 'nada'  or cabecera[2] == 'nada'  or cabecera[3] == 'nada'  or cabecera[4] == 'nada':
        return False

    fecha_inicio = cabecera[3]
    fecha_fin = cabecera[4]

    if fecha_inicio.month != fecha_fin.month:
        return False

    return True


def get_asignaturas(cabecera, repetidas):
    with open(RUTA_CSV, 'r', encoding='utf-8') as csvFile:
        reader = csv.reader(csvFile)

        for row in reader:
            line = row[0]
            pos = line.find(literal_materia)
            if pos != -1:
                pos_fin = line.find(literal_fin_materia)
                materia = line[pos + len(literal_materia): pos_fin]

                if materia in repetidas:
                    pos = repetidas.index(materia)
                    global contRepetida
                    contRepetida[pos] += 1
                    materia = materia[:-2] + str(contRepetida[pos]) + " "

            else:
                x = re.search(expresion_alumno, line)
                if x:
                    persistir_alumno(cabecera, materia, line)


def persistir_alumno(cabecera, materia, line):
    nombre = re.findall(expresion_nombre_alumno, line)
    horas = re.findall(expresion_horas_asignatura, line)
    porcentaje = re.findall(expresion_porcentaje_asignatura, line)

    porcentaje_just_float = porcentaje[0][:-1]
    porcentaje_just_float = porcentaje_just_float.replace(',', '.')

    porcentaje_injust_float = porcentaje[1][:-1]
    porcentaje_injust_float = porcentaje_injust_float.replace(',', '.')

    apercibimiento = None

    existe = AsignaturasEspeciales.objects.filter(materia=materia[:-1]).exists()

    if (existe and float(porcentaje_injust_float) >= 50) or (not existe and float(porcentaje_injust_float) >= 25):
        apercibimiento = Apercibimiento(alumno=nombre[0][:-1], periodo_academico=cabecera[0],
                                        curso=cabecera[1][:-1],
                                        unidad=cabecera[2], materia=materia[:-1], fecha_inicio=cabecera[3],
                                        fecha_fin=cabecera[4],
                                        horas_justificadas=horas[0], porcentaje_justificado=porcentaje_just_float,
                                        horas_injustificadas=horas[1],
                                        porcentaje_injustificado=porcentaje_injust_float,
                                        retrasos=horas[2])

    if apercibimiento is not None and not comprobar_repetido(apercibimiento):
        apercibimiento.save()


def comprobar_repetido(nuevo_apercibimiento):
    repetido = False
    apercibimientos = Apercibimiento.objects.all()

    for apercibimiento in apercibimientos:
        #Comprobación pequeña antes de comprobar el objeto entero para agilizar el proceso
        if nuevo_apercibimiento.fecha_inicio == apercibimiento.fecha_inicio:
            if nuevo_apercibimiento == apercibimiento:
                repetido = True

    return repetido
