from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.http import HttpResponse
from django.shortcuts import render

# Create your views here.
from .models import Profesor


@login_required
def createuser(request):
    if request.method == "POST":
        userName = request.POST.get('username', None)
        print(userName)
        userPass = request.POST.get('password', None)
        print(userPass)
        userMail = request.POST.get('email', None)
        print(userMail)
        if userName and userPass and userMail:
            if User.objects.filter(username=userName).exists():
                return HttpResponse('Duplicate username')
            user = User.objects.create_user(username=userName, email=userMail, password=userPass)

            dni = request.POST.get('dni', None)
            nombre = request.POST.get('nombre', None)
            apellidos = request.POST.get('apellidos', None)
            curso = request.POST.get('curso', None)

            print("guardar")
            if dni and nombre and apellidos and curso:
                p = Profesor(dni=dni, nombre=nombre, apellidos=apellidos, cursoTutor=curso, usuario=user)
                p.save()
                return HttpResponse('success')

        return HttpResponse('failure')

