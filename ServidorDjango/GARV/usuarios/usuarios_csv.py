import csv

from django.contrib.auth.models import User

from visitas.models import Profesor


def leercsv(filepath):
    print()
    with open(filepath) as csv_file:
        csv_reader = csv.reader(csv_file)
        for row in csv_reader:
            nombre = row[0]
            apellidos = row[1]
            usuario = row[2]
            contrasena = row[3]
            correo = row[4]
            dni = row[5]
            cursoTutor = ''
            if len(row) == 7:
                cursoTutor = row[6]
            registrar_usuario(nombre, apellidos, usuario, contrasena, correo, dni, cursoTutor)


def registrar_usuario(nombre, apellidos, usuario, contrasena, correo, dni, cursoTutor):
    if usuario and contrasena and correo and dni and nombre and apellidos:
        if not User.objects.filter(username=usuario).exists() and not Profesor.objects.filter(dni=dni).exists():
            user = User.objects.create_user(username=usuario, email=correo, password=contrasena)
            user.first_name = nombre
            user.last_name = apellidos

            p = Profesor(dni=dni, cursoTutor=cursoTutor, usuario=user)
            user.save()
            p.save()
