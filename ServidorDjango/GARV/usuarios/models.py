from django.db import models


class Document(models.Model):
    """
    Modelo de la tabla Document, guarda la ruta al archivo subido
    """
    docfile = models.FileField(upload_to='documents/%Y/%m/%d')