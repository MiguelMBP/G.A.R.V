from base64 import b64decode

from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.files.base import ContentFile
from django.db import IntegrityError
from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render
from django.urls import reverse
from django.utils.timezone import now

from usuarios.models import Document
from usuarios.usuarios_csv import leercsv
from usuarios.forms import DocumentForm
from visitas.models import Profesor


def a_login(request):
    msg = []
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        user = authenticate(username=username, password=password)
        if user is not None:
            if user.is_active:
                login(request, user)
                msg.append("login successful")
            else:
                msg.append("disabled account")
        else:
            msg.append("invalid login")
    return HttpResponse(msg)


@login_required
def createuser(request):
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
def updateuser(request):
    if request.method == 'POST':
        username = request.POST.get('username', None)
        mail = request.POST.get('email', None)
        dni = request.POST.get('dni', None)
        nombre = request.POST.get('nombre', None)
        apellidos = request.POST.get('apellidos', None)
        curso = request.POST.get('curso', None)

        if dni and nombre and apellidos and mail and username:
            user = User.objects.get(username=username)
            p = Profesor.objects.filter(usuario=user).first()
            user.first_name = nombre
            user.last_name = apellidos
            user.email = mail
            p.dni = dni
            p.cursoTutor = curso
            try:
                user.save()
                p.save()
            except IntegrityError:
                return HttpResponse('dni repetido')

            return HttpResponse('success')
    return HttpResponse('failure')



@login_required
def changePass(request):
    if request.method == "POST":
        username = request.POST.get('username', None)
        password = request.POST.get('password', None)

        if username and password:
            u = User.objects.get(username=username)
            u.set_password(password)
            u.save()

            return HttpResponse('success')

        return HttpResponse('failure')

@login_required
def usuarios(request):
    users = User.objects.all()
    usuarios = []
    for user in users:
        profesor = Profesor.objects.values('dni', 'cursoTutor').filter(usuario=user).first()
        usuarios.append(Usuario(usuario=user, profesor=profesor))
    return render(request, 'usuarios.html', {'usuarios': usuarios})


@login_required
def editarUsuario(request, id):
    usuario = User.objects.get(id=id)
    profesor = Profesor.objects.filter(usuario=usuario).first()
    return render(request, 'editarUsuarios.html', {'usuario': usuario, 'profesor': profesor})


@login_required
def crearUsuario(request):
    return render(request, 'crearUsuario.html')


@login_required
def importarusuarios(request):
    if request.method == 'POST':
        form = DocumentForm(request.POST, request.FILES)
        if form.is_valid():
            newdoc = Document(docfile=request.FILES['docfile'])
            newdoc.save()

            file = request.FILES['docfile'].name

            if file.endswith('.csv'):
                leercsv(newdoc.docfile.path)

            return HttpResponseRedirect(reverse('usuarios'))
    else:
        form = DocumentForm()

    return render(request, 'importar.html', {'form': form})


@login_required
def importarPost(request):
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