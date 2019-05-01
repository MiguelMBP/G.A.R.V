from base64 import decodestring, b64decode

from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.files.base import ContentFile
from django.core.files.images import ImageFile
from django.http import HttpResponse
from django.shortcuts import render

# Create your views here.
from .models import Profesor, Visita


@login_required
def createuser(request):
    if request.method == "POST":
        userName = request.POST.get('username', None)
        userPass = request.POST.get('password', None)
        userMail = request.POST.get('email', None)

        if userName and userPass and userMail:
            if User.objects.filter(username=userName).exists():
                return HttpResponse('Duplicate username')
            user = User.objects.create_user(username=userName, email=userMail, password=userPass)

            dni = request.POST.get('dni', None)
            nombre = request.POST.get('nombre', None)
            apellidos = request.POST.get('apellidos', None)
            curso = request.POST.get('curso', None)

            if dni and nombre and apellidos and curso:
                p = Profesor(dni=dni, nombre=nombre, apellidos=apellidos, cursoTutor=curso, usuario=user)
                p.save()
                return HttpResponse('success')

        return HttpResponse('failure')

@login_required
def registervisit(request):
    if request.method == "POST":
        userId = request.POST.get('userId', None)
        date = request.POST.get('date', None)
        img = request.POST.get('img', None)
        studentId = request.POST.get('studentId', None)

        if userId and date and img and studentId:
            imgFile = ContentFile(b64decode(img), name=userId + '_' + studentId + '_' + date + '.png')
            visita = Visita(fecha=date, imagen=imgFile, alumno_id=studentId, profesor_id=userId)
            visita.save()

            print("guardar")

        return HttpResponse('failure')
