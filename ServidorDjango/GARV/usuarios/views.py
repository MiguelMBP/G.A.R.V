import random
from base64 import b64decode

from django.contrib.admin.views.decorators import staff_member_required
from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.files.base import ContentFile
from django.db import IntegrityError
from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render
from django.urls import reverse
from django.utils.timezone import now

from usuarios.forms import DocumentForm
from usuarios.models import Document
from usuarios.usuarios_csv import leercsv
from visitas.models import Profesor


def a_login(request):
    """
    Realiza un login desde POST
    :param request:
    :return:
    """
    msg = []
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        user = authenticate(username=username, password=password)
        if user is not None:
            if user.is_active:
                if not Profesor.objects.filter(usuario=user).exists():
                    Profesor(dni=random.randint(11111111, 99999999), cursoTutor="", usuario=user).save()
                login(request, user)
                msg.append("login successful")
            else:
                msg.append("disabled account")
        else:
            msg.append("invalid login")
    return HttpResponse(msg)


@login_required
@staff_member_required
def createuser(request):
    """
    Crea un usuario mediante POST
    :param request:
    :return:
    """
    if request.method == "POST":
        userName = request.POST.get('username', None)
        userPass = request.POST.get('password', None)
        userMail = request.POST.get('email', None)
        dni = request.POST.get('dni', None)
        nombre = request.POST.get('nombre', None)
        apellidos = request.POST.get('apellidos', None)
        curso = request.POST.get('curso', None)

        if userName and userPass and userMail:
            if User.objects.filter(username=userName).exists():
                return HttpResponse('Duplicate username')
            if Profesor.objects.filter(dni=dni).exists():
                return HttpResponse('dni repetido')

            user = User.objects.create_user(username=userName, email=userMail, password=userPass)

            if dni and nombre and apellidos:
                user.first_name = nombre
                user.last_name = apellidos

                p = Profesor(dni=dni, cursoTutor=curso, usuario=user)
                user.save()
                p.save()
                return HttpResponse('success')

        return HttpResponse('failure')


@login_required
@staff_member_required
def updateuser(request):
    """
    Actualiza un usuario recibido por POST
    :param request:
    :return:
    """
    if request.method == 'POST':
        username = request.POST.get('username', None)
        mail = request.POST.get('email', None)
        dni = request.POST.get('dni', None)
        nombre = request.POST.get('nombre', None)
        apellidos = request.POST.get('apellidos', None)
        curso = request.POST.get('curso', None)

        if dni and nombre and apellidos and mail and username:
            user = User.objects.get(username=username)

            user.first_name = nombre
            user.last_name = apellidos
            user.email = mail

            if Profesor.objects.filter(usuario=user).exists():
                p = Profesor.objects.filter(usuario=user).first()
                p.dni = dni
                p.cursoTutor = curso
            else:
                p = Profesor(dni=dni, cursoTutor=curso, usuario=user)

            try:
                user.save()
                p.save()
            except IntegrityError:
                return HttpResponse('dni repetido')

            return HttpResponse('success')
    return HttpResponse('failure')


@login_required
def is_staff(request):
    """
    Comprueba si un usuario es Staff
    :param request:
    :return:
    """
    username = request.POST.get('username', None)
    if username:
        if User.objects.filter(username=username).exists():
            u = User.objects.get(username=username)
            staff = u.is_staff
            return HttpResponse(staff)
    return HttpResponse(False)


@login_required
def changePass(request):
    """
    Cambia la contraseña de un usuario
    :param request:
    :return:
    """
    if request.method == "POST":
        username = request.POST.get('username', None)
        password = request.POST.get('password', None)

        if username and password:
            if request.user.is_authenticated:
                user = request.user
                if user.username == username or user.is_staff:
                    u = User.objects.get(username=username)
                    u.set_password(password)
                    u.save()
                    return HttpResponse('success')

        return HttpResponse('failure')


@login_required
@staff_member_required
def usuarios(request):
    """
    Renderiza la página de usuarios
    :param request:
    :return:
    """
    users = User.objects.all()
    usuarios = []
    for user in users:
        profesor = Profesor.objects.values('dni', 'cursoTutor').filter(usuario=user).first()
        usuarios.append(Usuario(usuario=user, profesor=profesor))
    return render(request, 'usuarios.html', {'usuarios': usuarios})


@login_required
@staff_member_required
def eliminarusuarios(request):
    """
    Elimina un conjunto de usuarios pasados por POST
    :param request:
    :return:
    """
    if request.method == "POST":
        users = request.POST.getlist('users[]', None)
        username = request.POST.get('username', None)
        if users:
            for id in users:
                user = User.objects.get(id=id)
                if not user.is_superuser:
                    user.delete()
        elif username:
            user = User.objects.get(username=username)
            if not user.is_superuser:
                user.delete()
                return HttpResponse("Eliminado")

    return HttpResponse("Finalizado")



@login_required
@staff_member_required
def editarUsuario(request, id):
    """
    Edita un usuario recibido del url
    :param request:
    :param id:
    :return:
    """
    usuario = User.objects.get(id=id)
    profesor = Profesor.objects.filter(usuario=usuario).first()
    return render(request, 'editarUsuarios.html', {'usuario': usuario, 'profesor': profesor})


@login_required
@staff_member_required
def crearUsuario(request):
    """
    Renderiza la página de crear un usuario
    :param request:
    :return:
    """
    return render(request, 'crearUsuario.html')


@login_required
@staff_member_required
def importarusuarios(request):
    """
    Recibe del formulario un documento csv para analizarlo y registrar los usuarios en su interior
    :param request:
    :return:
    """
    if request.method == 'POST':
        form = DocumentForm(request.POST, request.FILES)
        if form.is_valid():
            newdoc = Document(docfile=request.FILES['docfile'])
            newdoc.docfile.name = str(now) + '.csv'
            newdoc.save()

            file = request.FILES['docfile'].name

            if file.endswith('.csv'):
                leercsv(newdoc.docfile.path)

            return HttpResponseRedirect(reverse('usuarios'))
    else:
        form = DocumentForm()

    return render(request, 'importar.html', {'form': form})


@login_required
@staff_member_required
def importarPost(request):
    """
    Recibe un documento csv cifrado en base64 para analizarlo y registrar los usuarios en su interior
    :param request:
    :return:
    """
    if request.method == 'POST':
        base64 = request.POST.get('archivo', None)
        if base64:
            archivo = ContentFile(b64decode(base64), name=str(now) + '.csv')
            newdoc = Document(docfile=archivo)
            newdoc.save()
            leercsv(newdoc.docfile.path)
    return HttpResponse('success')


class Usuario:
    def __init__(self, usuario, profesor):
        self.usuario = usuario
        self.profesor = profesor
