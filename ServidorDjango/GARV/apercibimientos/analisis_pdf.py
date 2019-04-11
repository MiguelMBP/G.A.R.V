import csv
import datetime
import re

import tabula

from .models import Apercibimiento

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


def pdf_to_csv(filepath):
    tabula.convert_into(filepath, "output.csv", pages="all", output_format="csv", guess=False, stream=True)
    # tabula.convert_into("a.pdf", "outputFormat.csv", pages="all", output_format="csv", stream=True)

    leer_csv()


def leer_csv():
    cabecera = get_cabecera()

    print("\nCabecera")
    print(cabecera[0])
    print(cabecera[1])
    print(cabecera[2])
    print(cabecera[3])
    print(cabecera[4])

    print("\nAsignaturas")
    get_asignaturas(cabecera)


def get_cabecera():
    anno = 'nada'
    curso = 'nada'
    unidad = 'nada'
    fecha_inicio = 'nada'
    fecha_fin = 'nada'

    with open('output.csv', 'r') as csvFile:
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


def get_asignaturas(cabecera):
    with open('output.csv', 'r') as csvFile:
        reader = csv.reader(csvFile)

        for row in reader:
            line = row[0]
            pos = line.find(literal_materia)
            if pos != -1:
                pos_fin = line.find(literal_fin_materia)
                materia = line[pos + len(literal_materia): pos_fin]
                print(materia)
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

    if float(porcentaje_injust_float) >= 25:
        print('\t', nombre[0][:-1], ' Horas justificadas:', horas[0], ' Porcentaje justificado:', porcentaje[0],
              ' Horas injustificadas:', horas[1], ' Porcentaje injustificado:', porcentaje[1], ' Retrasos:', horas[2], 'X')

        apercibimiento = Apercibimiento(alumno=nombre[0][:-1], periodo_academico=cabecera[0], curso=cabecera[1],
                                        unidad=cabecera[2], materia=materia, fecha_inicio=cabecera[3],
                                        fecha_fin=cabecera[4],
                                        horas_justificadas=horas[0], porcentaje_justificado=porcentaje_just_float,
                                        horas_injustificadas=horas[1], porcentaje_injustificado=porcentaje_injust_float,
                                        retrasos=horas[2])

        if not comprobar_repetido(apercibimiento):
            apercibimiento.save()

    else:
        print('\t', nombre[0][:-1], ' Horas justificadas:', horas[0], ' Porcentaje justificado:', porcentaje[0],
              ' Horas injustificadas:', horas[1], ' Porcentaje injustificado:', porcentaje[1], ' Retrasos:', horas[2])


def comprobar_repetido(nuevo_apercibimiento):
    repetido = False
    apercibimientos = Apercibimiento.objects.all()
    for apercibimiento in apercibimientos:
        if apercibimiento == nuevo_apercibimiento:
            repetido = True
            break

    return repetido


if __name__ == '__main__':
    pdf_to_csv("a.pdf")
