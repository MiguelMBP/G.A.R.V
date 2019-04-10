from django.db import models


class Document(models.Model):
    docfile = models.FileField(upload_to='documents/%Y/%m/%d')


class Apercibimiento(models.Model):
    alumno = models.CharField(max_length=200)
    periodo_academico = models.IntegerField()
    curso = models.CharField(max_length=200)
    unidad = models.CharField(max_length=200)
    asignatura = models.CharField(max_length=200)
    fecha_inicio = models.DateField()
    fecha_fin = models.DateField()
    horas_justificadas = models.CharField(max_length=200)
    porcentaje_justificado = models.FloatField()
    horas_injustificadas = models.CharField(max_length=200)
    porcentaje_injustificado = models.FloatField()
    retrasos = models.CharField(max_length=200)
