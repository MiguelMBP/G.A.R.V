from django import forms
from django.forms import ModelForm

from .models import Apercibimiento


class DocumentForm(forms.Form):
    """
    Formulario para subir un archivo
    """
    docfile = forms.FileField(
        label='Seleccione un archivo',
        help_text='max. 42 megabytes'
    )


class AuthorForm(ModelForm):
    class Meta:
        model = Apercibimiento
        fields = '__all__'
