from django.conf import settings
from django.db import models


class Profesor(models.Model):
    dni = models.CharField(max_length=9, unique=True)
    nombre = models.CharField(max_length=100)
    apellidos = models.CharField(max_length=100)
    cursoTutor = models.CharField(max_length=100)
    usuario = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE,)


class Alumno(models.Model):
    dni = models.CharField(max_length=9, unique=True)
    nombre = models.CharField(max_length=100)
    apellidos = models.CharField(max_length=100)
    curso = models.CharField(max_length=100)


class Empresa(models.Model):
    cif = models.CharField(max_length=9, unique=True)
    nombre = models.CharField(max_length=100)
    poblacion = models.CharField(max_length=100)
    direccion = models.CharField(max_length=100)
    coordenadaX = models.FloatField()
    coordenadaY = models.FloatField()


class Visita(models.Model):
    profesor = models.ForeignKey(Profesor, on_delete=models.CASCADE)
    alumno = models.ForeignKey(Alumno, on_delete=models.CASCADE)
    empresa = models.ForeignKey(Empresa, on_delete=models.CASCADE)
    fecha = models.DateField()
    distancia = models.FloatField()
    imagen = models.CharField(max_length=200)

