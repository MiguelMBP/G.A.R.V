from django import forms


class DocumentForm(forms.Form):
    docfile = forms.FileField(
        label='Seleccionar un archivo CSV.',
        help_text='Debe contener las columnas Nombre, Apellidos, Usuario, Contraseña, Correo, DNI y Curso Tutor, en ese orden'
    )