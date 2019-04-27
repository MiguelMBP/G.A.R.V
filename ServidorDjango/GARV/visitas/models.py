from django.conf import settings
from django.db import models


class Profesor(models.Model):
    dni = models.CharField(max_length=9, unique=True)
    nombre = models.CharField(max_length=100)
    apellidos = models.CharField(max_length=100)
    cursoTutor = models.CharField(max_length=100, null=True)
    usuario = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE,)


class Empresa(models.Model):
    cif = models.CharField(max_length=9, unique=True)
    nombre = models.CharField(max_length=100)
    poblacion = models.CharField(max_length=100)
    direccion = models.CharField(max_length=100)
    latitud = models.FloatField()
    longitud = models.FloatField()
    distancia = models.FloatField(default=0)


class Alumno(models.Model):
    dni = models.CharField(max_length=9, unique=True)
    nombre = models.CharField(max_length=100)
    apellidos = models.CharField(max_length=100)
    curso = models.CharField(max_length=100)
    empresa = models.ForeignKey(Empresa, on_delete=models.CASCADE, default="-1")


class Visita(models.Model):
    profesor = models.ForeignKey(Profesor, on_delete=models.CASCADE)
    alumno = models.ForeignKey(Alumno, on_delete=models.CASCADE)
    fecha = models.DateField()
    imagen = models.ImageField()
    validada = models.BooleanField(default=False)

