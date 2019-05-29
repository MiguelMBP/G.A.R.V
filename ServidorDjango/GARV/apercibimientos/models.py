from django.db import models


class Document(models.Model):
    """
    Modelo de la tabla Document, guarda la ruta al archivo subido
    """
    docfile = models.FileField(upload_to='documents/%Y/%m/%d')


class Apercibimiento(models.Model):
    """
    Modelo de la tabla Apercibimiento, guarda todos los datos referente a él
    """
    alumno = models.CharField(max_length=200)
    periodo_academico = models.IntegerField()
    curso = models.CharField(max_length=200)
    unidad = models.CharField(max_length=200)
    materia = models.CharField(max_length=200)
    fecha_inicio = models.DateField()
    fecha_fin = models.DateField()
    horas_justificadas = models.CharField(max_length=200)
    porcentaje_justificado = models.DecimalField(decimal_places=2, max_digits=6)
    horas_injustificadas = models.CharField(max_length=200)
    porcentaje_injustificado = models.DecimalField(decimal_places=2, max_digits=6)
    retrasos = models.CharField(max_length=200)
    activo = models.BooleanField(default=True)

    def __eq__(self, other):
        if not isinstance(other, Apercibimiento):
            # don't attempt to compare against unrelated types
            return False

        return self.alumno == other.alumno and int(self.periodo_academico) == int(other.periodo_academico) \
               and self.curso == other.curso and self.unidad == other.unidad and self.materia == other.materia \
               and self.fecha_inicio == other.fecha_inicio and self.fecha_fin == other.fecha_fin \
               and self.horas_justificadas == other.horas_justificadas and float(self.porcentaje_justificado) == float(other.porcentaje_justificado) \
               and self.horas_injustificadas == other.horas_injustificadas and float(self.porcentaje_injustificado) == float(other.porcentaje_injustificado) \
               and self.retrasos == other.retrasos

    def __str__(self):
        return self.alumno + " " + str(self.periodo_academico) + " " + self.curso + " " + self.unidad + " " + self.materia + " " + str(self.fecha_inicio) + " " +\
               str(self.fecha_fin) + " " + self.horas_justificadas + " " + str(self.porcentaje_justificado) + " " + self.horas_injustificadas + " " + str(self.porcentaje_injustificado) + " " + self.retrasos


class AsignaturaEspecial(models.Model):
    """
    Modelo de la tabla AsignaturaEspecial
    """
    materia = models.CharField(max_length=200)

    def __eq__(self, other):
        if not isinstance(other, AsignaturaEspecial):
            # don't attempt to compare against unrelated types
            return False

        return self.materia == other.materia

    def __str__(self):
        return self.materia
