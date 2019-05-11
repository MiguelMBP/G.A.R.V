import csv
from base64 import b64decode, b64encode

from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.files.base import ContentFile
from django.http import HttpResponse
from django.shortcuts import render

# Create your views here.
from django.views.decorators.csrf import csrf_exempt

from .models import Profesor, Visita, Empresa, Alumno


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
        print(date)
        img = request.POST.get('img', None)
        studentId = request.POST.get('studentId', None)

        if userId and date and img and studentId:
            imgFile = ContentFile(b64decode(img), name=userId + '_' + studentId + '_' + date + '.jpg')
            visita = Visita(fecha=date, imagen=imgFile, alumno_id=studentId, profesor_id=userId)
            visita.save()

            return HttpResponse('success')

        return HttpResponse('failure')


@login_required
def sendimage(request):
    if request.method == "POST":
        id = request.POST.get('id', None)

        if id:
            visita = Visita.objects.get(id=id)
            with open(visita.imagen.path, "rb") as image_file:
                encoded_string = b64encode(image_file.read())

                return HttpResponse(encoded_string)

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
def resumenVisitas(request):
    if request.GET and 'valor' in request.GET:
        valor = request.GET['valor']
        response = HttpResponse(content_type='text/csv')
        response['Content-Disposition'] = 'attachment; filename="visitas.csv"'

        writer = csv.writer(response)
        writer.writerow(["Nombre", "Distancia Total", "Importe"])
        profesores = Visita.objects.values('profesor').distinct()

        for profesor in profesores:
            visitas = Visita.objects.all().filter(profesor=profesor['profesor'], validada=True)
            profesorObj = Profesor.objects.all().filter(id=profesor['profesor'])
            distanciaTotal = 0.0
            for visita in visitas:
                empresa = Empresa.objects.all().filter(id=visita.alumno.empresa.id)
                distancia = empresa[0].distancia
                distanciaTotal += float(distancia)
            importe = distanciaTotal * float(valor)
            writer.writerow([profesorObj[0].nombre, distanciaTotal, importe])

        return response
