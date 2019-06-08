import csv
from base64 import b64decode, b64encode
from datetime import datetime

from django.contrib.admin.views.decorators import staff_member_required
from django.contrib.auth.decorators import login_required
from django.core.files.base import ContentFile
from django.http import HttpResponse, JsonResponse
from django.shortcuts import render

from .models import Profesor, Visita, Empresa, Alumno


@login_required
@staff_member_required
def sendimage(request):
    """
    Envía la imagen en base64 de la visita recibida por POST
    :param request:
    :return:
    """
    if request.method == "POST":
        id = request.POST.get('id', None)

        if id:
            visita = Visita.objects.get(id=id)
            with open(visita.imagen.path, "rb") as image_file:
                encoded_string = b64encode(image_file.read())

                return HttpResponse(encoded_string)

        return HttpResponse('failure')


@login_required
def registervisit(request):
    """
    Registra una visita con los datos recibidos por POST
    :param request:
    :return:
    """
    if request.method == "POST":
        userId = request.POST.get('userId', None)
        date = request.POST.get('date', None)
        img = request.POST.get('img', None)
        studentId = request.POST.get('studentId', None)

        if userId and date and img and studentId:
            imgFile = ContentFile(b64decode(img), name=userId + '_' + studentId + '_' + date + '.jpg')
            visita = Visita(fecha=date, imagen=imgFile, alumno_id=studentId, profesor_id=userId)
            visita.save()

            return HttpResponse('success')

        return HttpResponse('failure')


@login_required
@staff_member_required
def resumenVisitas(request):
    """
    Genera un documento CSV con el importe correspondiente de cada profesor y los kilómetros totales de las visitas
    :param request:
    :return:
    """
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
            writer.writerow([profesorObj[0].usuario.first_name + ' ' + profesorObj[0].usuario.last_name, round(distanciaTotal, 2), round(importe, 2)])

        return response


@login_required
@staff_member_required
def visitas(request):
    """
    Renderiza la página de visitas
    :param request:
    :return:
    """
    visitas = list(Visita.objects.values('profesor').distinct())
    profesores = []
    for visita in visitas:
        profesores.append(Profesor.objects.get(id=visita['profesor']))
    lista = Visita.objects.all().order_by('profesor', 'fecha')

    for visita in lista:
        fecha = str(datetime.strptime(str(visita.fecha), "%Y-%M-%d").strftime('%d-%m-%Y'))
        visita.fecha = fecha
    return render(request, 'visitas.html', {'profesores': profesores, 'visitas': lista})


@login_required
@staff_member_required
def empresas(request):
    """
    Renderiza la página de empresas
    :param request:
    :return:
    """
    empresas = Empresa.objects.all()
    return render(request, 'empresas.html', {'empresas': empresas})


@login_required
@staff_member_required
def editarEmpresa(request, id):
    """
    Edita la empresa recibida por POST
    :param request:
    :param id:
    :return:
    """
    if request.POST and request.is_ajax():
        nombre = request.POST.get('nombre', None)
        cif = request.POST.get('cif', None)
        poblacion = request.POST.get('poblacion', None)
        direccion = request.POST.get('direccion', None)
        latitud = request.POST.get('latitud', None)
        longitud = request.POST.get('longitud', None)
        distancia = request.POST.get('distancia', None)

        if nombre and cif and poblacion and direccion and latitud and longitud and distancia:
            temp = Empresa.objects.filter(cif=cif)
            if temp.exists() and temp.first().id != id:
                return HttpResponse('Duplicate cif')
            empresa = Empresa.objects.get(id=id)
            empresa.nombre = nombre
            empresa.cif = cif
            empresa.poblacion = poblacion
            empresa.direccion = direccion
            empresa.latitud = latitud
            empresa.longitud = longitud
            empresa.distancia = distancia
            empresa.save()
            return HttpResponse('ok')
        return HttpResponse('error')
    else:
        empresa = Empresa.objects.get(id=id)
        return render(request, 'modificarEmpresa.html', {'empresa': empresa})


@login_required
@staff_member_required
def editarAlumno(request, id):
    """
    Edita el alumno pasado por POST
    :param request:
    :param id:
    :return:
    """
    if request.POST and request.is_ajax():
        nombre = request.POST.get('nombre', None)
        dni = request.POST.get('dni', None)
        apellidos = request.POST.get('apellidos', None)
        curso = request.POST.get('curso', None)
        empresa_id = request.POST.get('empresa', None)

        if nombre and dni and apellidos and curso and empresa_id:
            temp = Alumno.objects.filter(dni=dni)
            if temp.exists() and temp.first().dni != dni:
                return HttpResponse('Duplicate dni')
            empresa = Empresa.objects.get(id=empresa_id)
            alumno = Alumno.objects.get(id=id)
            alumno.nombre = nombre
            alumno.dni=dni
            alumno.apellidos=apellidos
            alumno.curso=curso
            alumno.empresa=empresa
            alumno.save()
            return HttpResponse('ok')
        return HttpResponse('error')
    else:
        empresas = Empresa.objects.all()
        alumno = Alumno.objects.get(id=id)
        return render(request, 'modificarAlumno.html', {'empresas': empresas, 'alumno': alumno})


@login_required
@staff_member_required
def crearEmpresa(request):
    """
    Crea la empresa pasada por POST
    :param request:
    :return:
    """
    if request.POST and request.is_ajax():
        nombre = request.POST.get('nombre', None)
        cif = request.POST.get('cif', None)
        poblacion = request.POST.get('poblacion', None)
        direccion = request.POST.get('direccion', None)
        latitud = request.POST.get('latitud', None)
        longitud = request.POST.get('longitud', None)
        distancia = request.POST.get('distancia', None)

        if nombre and cif and poblacion and direccion and latitud and longitud and distancia:
            if Empresa.objects.filter(cif=cif):
                return HttpResponse('Duplicate cif')
            Empresa(nombre=nombre, cif=cif, poblacion=poblacion, direccion=direccion, latitud=latitud, longitud=longitud, distancia=distancia).save()
            return HttpResponse('ok')
        return HttpResponse('error')
    else:
        return render(request, 'crearEmpresa.html')


@login_required
@staff_member_required
def crearAlumno(request):
    """
    Crea el alumno pasado por POST
    :param request:
    :return:
    """
    if request.POST and request.is_ajax():
        nombre = request.POST.get('nombre', None)
        dni = request.POST.get('dni', None)
        apellidos = request.POST.get('apellidos', None)
        curso = request.POST.get('curso', None)
        empresa_id = request.POST.get('empresa', None)

        if nombre and dni and apellidos and curso and empresa_id:
            if Alumno.objects.filter(dni=dni):
                return HttpResponse('Duplicate dni')
            empresa = Empresa.objects.get(id=empresa_id)
            Alumno(nombre=nombre, apellidos=apellidos, dni=dni, curso=curso, empresa=empresa).save()
            return HttpResponse('ok')
        return HttpResponse('error')
    else:
        empresas = Empresa.objects.all()
        return render(request, 'crearAlumno.html', {'empresas': empresas})


@login_required
@staff_member_required
def alumnos(request):
    """
    Renderiza la página de alumnos
    :param request:
    :return:
    """
    alumnos = Alumno.objects.all()
    return render(request, 'alumnos.html', {'alumnos': alumnos})


@login_required
@staff_member_required
def getVisitas(request):
    """
    Recoge las visitas de un profesor
    :param request:
    :return:
    """
    if request.GET and 'profesor' in request.GET:
        profesor = request.GET['profesor']
        visitas = Visita.objects.filter(profesor_id=profesor)
        respuesta = []
        for visita in visitas:
            fecha = datetime.strptime(str(visita.fecha), "%Y-%M-%d").strftime('%d-%m-%Y')
            fila = VisitaTabla(id=visita.id, profesor=visita.profesor.usuario.first_name + ' ' + visita.profesor.usuario.last_name, alumno=visita.alumno.nombre + ' ' + visita.alumno.apellidos, empresa=visita.alumno.empresa.nombre, fecha=str(fecha), validada=visita.validada)
            respuesta.append(fila.as_json())
        return JsonResponse(respuesta, safe=False)
    else:
        return HttpResponse('error')


@login_required
@staff_member_required
def actualizarVisita(request):
    """
    Valida o invalida una visita recibida por GET y ajax
    :param request:
    :return:
    """
    if request.GET and request.is_ajax() and 'visita' in request.GET:
        id = request.GET['visita']
        visita = Visita.objects.filter(id=id).first()
        visita.validada = not visita.validada
        visita.save()
        profesor = request.GET['profesor']
        print(profesor)
        if profesor.isdigit():
            visitas = Visita.objects.filter(profesor_id=profesor)
        else:
            visitas = Visita.objects.all()
        respuesta = []
        for visita in visitas:
            fecha = datetime.strptime(str(visita.fecha), "%Y-%M-%d").strftime('%d-%m-%Y')
            fila = VisitaTabla(id=visita.id,
                               profesor=visita.profesor.usuario.first_name + ' ' + visita.profesor.usuario.last_name,
                               alumno=visita.alumno.nombre + ' ' + visita.alumno.apellidos,
                               empresa=visita.alumno.empresa.nombre, fecha=str(fecha),
                               validada=visita.validada)
            respuesta.append(fila.as_json())
        return JsonResponse(respuesta, safe=False)
    else:
        return HttpResponse('error')


@login_required
@staff_member_required
def verImagen(request):
    """
    Renderiza la página de detalle de una visita con su imagen
    :param request:
    :return:
    """
    if request.GET and 'id' in request.GET:
        id = request.GET['id']
        visita = Visita.objects.get(id=id)
        return render(request, 'imagenVisita.html', {'visita': visita})
    else:
        return HttpResponse('error')


class VisitaTabla:
    def __init__(self, id, profesor, alumno, empresa, fecha, validada):
        self.id = id
        self.profesor = profesor
        self.alumno = alumno
        self.empresa = empresa
        self.fecha = fecha
        self.validada = validada

    def as_json(self):
        return dict(
            id=self.id, profesor=self.profesor,
            alumno=self.alumno,
            empresa=self.empresa,
            validada=self.validada,
            fecha=self.fecha)


