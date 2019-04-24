from django import forms
from django.forms import ModelForm

from .models import Apercibimiento


class DocumentForm(forms.Form):
    docfile = forms.FileField(
        label='Select a file',
        help_text='max. 42 megabytes'
    )

class AuthorForm(ModelForm):
    class Meta:
        model = Apercibimiento
        fields = '__all__'
