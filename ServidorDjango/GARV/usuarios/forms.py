from django import forms


class DocumentForm(forms.Form):
    """
    Formulario para subir un archivo
    """
    docfile = forms.FileField(
        label='Seleccionar un archivo CSV.',
        help_text='Debe contener las columnas Nombre, Apellidos, Usuario, Contraseña, Correo, DNI y Curso Tutor, en ese orden'
    )