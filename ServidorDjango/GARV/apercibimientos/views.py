import datetime
import tempfile

from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from django.core.paginator import Paginator
from django.db.models.functions import TruncMonth
from django.shortcuts import render

from django.http import HttpResponse, JsonResponse
from django.http import HttpResponseRedirect
from django.template.loader import render_to_string
from django.urls import reverse
from django.views.decorators.csrf import csrf_exempt
from weasyprint import HTML

from .models import Document, Apercibimiento
from .forms import DocumentForm
from .extraer_zip import extractZip
from .analisis_pdf import pdf_to_csv


@login_required
def subir_pdf(request):
    # Handle file upload
    if request.method == 'POST':
        form = DocumentForm(request.POST, request.FILES)
        if form.is_valid():
            newdoc = Document(docfile = request.FILES['docfile'])
            newdoc.save()

            file = request.FILES['docfile'].name

            if file.endswith('.pdf'):
                print()
                pdf_to_csv(newdoc.docfile.path)
            elif file.endswith('.zip'):
                extractZip(newdoc.docfile.path)

            # Redirect to the document list after POST
            return HttpResponseRedirect(reverse('list'))
    else:
        form = DocumentForm() # A empty, unbound form

    # Load documents for the list page
    documents = Document.objects.all()

    # Render list page with the documents and the form
    return render(request, 'list.html', {'documents': documents, 'form': form})


@login_required
def mostrarApercibimientos(request):
    apercibimientos = Apercibimiento.objects.all()
    paginator = Paginator(apercibimientos, 100)

    page = request.GET.get('page')
    lista = paginator.get_page(page)

    return render(request, 'apercibimientos.html', {'lista': lista})


def a_login(request):
    msg = []
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        print(username, password)
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
def buscarApercibimiento(request):
    periodo = Apercibimiento.objects.values("periodo_academico").distinct().order_by("periodo_academico")
    return render(request, 'buscar_apercibimiento.html', {'years': periodo})


@login_required
def sacarCursos(request):
    if request.GET and request.is_ajax():
        periodo = request.GET['periodo']
        lista = list(Apercibimiento.objects.values("unidad").distinct().filter(periodo_academico=periodo).order_by('unidad'))
        return JsonResponse(lista, safe=False)
    else:
        return HttpResponse('error')


@login_required
def sacarAlumnos(request):
    if request.GET and request.is_ajax():
        periodo = request.GET['periodo']
        curso = request.GET['curso']
        lista = list(Apercibimiento.objects.values("alumno").distinct().filter(periodo_academico=periodo, unidad=curso).order_by('alumno'))
        return JsonResponse(lista, safe=False)
    else:
        return HttpResponse('error')


@login_required
def sacarApercibimientos(request):
    if request.GET and request.is_ajax():
        periodo = request.GET['periodo']
        curso = request.GET['curso']
        alumno = request.GET['alumno']
        lista = list(Apercibimiento.objects.values("id", "alumno", "periodo_academico", "unidad", "materia", "fecha_inicio", "fecha_fin", "activo").distinct().filter(periodo_academico=periodo,
                                                                               unidad=curso, alumno=alumno).order_by('fecha_inicio', 'materia'))
        return JsonResponse(lista, safe=False)

    return HttpResponse('error')


@login_required
def actualizarApercibimiento(request):
    if request.GET and request.is_ajax():
        id = request.GET['id']
        apercibimiento = Apercibimiento.objects.filter(id=id).first()
        apercibimiento.activo = not apercibimiento.activo
        apercibimiento.save()

        periodo = request.GET['periodo']
        curso = request.GET['curso']
        alumno = request.GET['alumno']
        if (id and periodo and curso and alumno):
            lista = list(Apercibimiento.objects.values("id", "alumno", "periodo_academico", "unidad", "materia", "fecha_inicio", "fecha_fin", "activo").distinct().filter(periodo_academico=periodo,
                                                                               unidad=curso, alumno=alumno).order_by('fecha_inicio', 'materia'))
            return JsonResponse(lista, safe=False)

    return HttpResponse('error')

@login_required
def informeNumeroApercibimiento(request, anno, mes, unidad, minimo):
    if 9 <= mes <= 12:
        fecha = datetime.datetime(anno, mes, 28)
    else:
        fecha = datetime.datetime(anno+1, mes, 28)

    fechacurso = datetime.datetime(anno, 9, 1)
    unidad = unidad.replace("_", " ")

    if unidad == 'todos':
        apercibimientos = Apercibimiento.objects.only("alumno", "unidad", "materia").filter(periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha]).order_by("unidad", "alumno", "materia")

    else:
        apercibimientos = Apercibimiento.objects.only("alumno", "unidad", "materia").filter(periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha], unidad=unidad).order_by("unidad", "alumno", "materia")

    lista = []
    for apercibimiento in apercibimientos:
        resultado = Apercibimiento.objects.filter(alumno=apercibimiento.alumno, unidad=apercibimiento.unidad, materia=apercibimiento.materia, periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha], activo=True).count() #recorre la lista de apercibimientos por cada alumno y materia

        if resultado >= minimo:
            apercibimiento = InformeNumero(alumno=apercibimiento.alumno, unidad=apercibimiento.unidad, materia=apercibimiento.materia, numero=resultado)
            if apercibimiento not in lista:
                lista.append(apercibimiento)

    html_string = render_to_string('informeNumeroApercibimiento.html', {'numero': minimo, 'fecha': sacarFecha(anno, mes), 'apercibimientos': lista})
    html = HTML(string=html_string)
    result = html.write_pdf()

    response = HttpResponse(content_type='application/pdf;')
    response['Content-Disposition'] = 'inline; filename=informeNumeroApercibimiento.pdf'
    response['Content-Transfer-Encoding'] = 'binary'

    with tempfile.NamedTemporaryFile(delete=True) as output:
        output.write(result)
        output.flush()
        output = open(output.name, 'rb')
        response.write(output.read())

    return response


@login_required
def informeApercibimientoIndividual(request, anno, mes, unidad):
    if 9 <= mes <= 12:
        fecha = datetime.datetime(anno, mes, 28)
        fechainicio = datetime.datetime(anno, mes, 1)
    else:
        fecha = datetime.datetime(anno + 1, mes, 28)
        fechainicio = datetime.datetime(anno+1, mes, 1)

    fechacurso = datetime.datetime(anno, 9, 1)
    unidad = unidad.replace("_", " ")

    if unidad == 'todos':
        apercibimientos = Apercibimiento.objects.values("alumno", "unidad").distinct().filter(periodo_academico=anno, fecha_inicio__range=[fechainicio, fecha]).order_by("unidad", "alumno") #todos los alumnos

    else:
        apercibimientos = Apercibimiento.objects.values("alumno", "unidad").distinct().filter(periodo_academico=anno, fecha_inicio__range=[fechainicio, fecha], unidad=unidad).order_by("unidad", "alumno") #todos los alumnos

    lista = []
    for apercibimiento in apercibimientos:
        resultados = Apercibimiento.objects.filter(alumno=apercibimiento['alumno'], unidad=apercibimiento['unidad'], periodo_academico=anno, fecha_inicio__range=[fechainicio, fecha], activo=True) #todas las asignaturas de un alumno con apercibimiento en ese mes

        listaAp = []
        for resultado in resultados:
            resultados2 = Apercibimiento.objects.filter(alumno=resultado.alumno, unidad=resultado.unidad, materia=resultado.materia, periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha])
            numero = len(resultados2)
            materia = resultado.materia
            faltas = resultado.horas_injustificadas[:-3]
            porcentaje = resultado.porcentaje_injustificado

            alumno = ApercibimientoIndividual(numero=numero, materia=materia, faltas=faltas, porcentaje=porcentaje)

            listaAp.append(alumno)      #contiene cada apercibimiento de un alumno

        lista.append(InformeIndividual(alumno=apercibimiento['alumno'], unidad=apercibimiento['unidad'], fecha=sacarFecha(anno, mes), apercibimientos=listaAp))

    html_string = render_to_string('informeApercibimientoIndividual.html', {'lista': lista})
    html = HTML(string=html_string)
    result = html.write_pdf()

    response = HttpResponse(content_type='application/pdf;')
    response['Content-Disposition'] = 'inline; filename=informeApercibimientoIndividual.pdf'
    response['Content-Transfer-Encoding'] = 'binary'

    with tempfile.NamedTemporaryFile(delete=True) as output:
        output.write(result)
        output.flush()
        output = open(output.name, 'rb')
        response.write(output.read())

    return response


@login_required
def informeResumenApercibimiento(request, anno, mes, unidad):
    if 9 <= mes <= 12:
        fecha = datetime.datetime(anno, mes, 28)
    else:
        fecha = datetime.datetime(anno + 1, mes, 28)

    fechacurso = datetime.datetime(anno, 9, 1)
    unidad = unidad.replace("_", " ")
    listacursos = []
    if unidad == 'todos':
        cursos = Apercibimiento.objects.values("unidad").distinct().filter(periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha]).order_by("unidad") #todos los cursos

    else:
        cursos = Apercibimiento.objects.values("unidad").distinct().filter(periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha], unidad=unidad).order_by("unidad") #curso por parametros

    for curso in cursos:
        lista = []
        materias = Apercibimiento.objects.values("materia", "unidad").distinct().filter(periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha], unidad=curso['unidad']).order_by("unidad", "materia") #todas las materias

        for materia in materias:
            listamat = []
            alumnos = Apercibimiento.objects.values("alumno").distinct().filter(materia=materia['materia'], unidad=materia['unidad'], periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha], activo=True) #todos los alumnos con apercibimiento en una asignatura

            for alumno in alumnos:
                #meses en los que ha faltado cada alumno por asignatura
                resultado = Apercibimiento.objects.annotate(month=TruncMonth('fecha_inicio')).values('month').filter(alumno=alumno["alumno"], materia=materia['materia'], unidad=materia['unidad'], periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha], activo=True).order_by('month')
                listamat.append(ApercibimientoMateria(alumno=alumno["alumno"], numero=len(resultado), meses=listarMeses(resultado)))

            lista.append(InformeResumen(materia=materia['materia'], alumnos=listamat))

        listacursos.append(InformeResumenCurso(unidad=curso['unidad'], fecha=sacarFecha(anno, mes), materias=lista))

    html_string = render_to_string('ResumenApercibimientosMateria.html', {'lista': listacursos})
    html = HTML(string=html_string)
    result = html.write_pdf()

    response = HttpResponse(content_type='application/pdf;')
    response['Content-Disposition'] = 'inline; filename=ResumenApercibimientosMateria.pdf'
    response['Content-Transfer-Encoding'] = 'binary'

    with tempfile.NamedTemporaryFile(delete=True) as output:
        output.write(result)
        output.flush()
        output = open(output.name, 'rb')
        response.write(output.read())

    return response


def sacarFecha(anno, mes):
    if mes == 9:
        return 'Septiembre de ' + str(anno)
    if mes == 10:
        return 'Octubre de ' + str(anno)
    if mes == 11:
        return 'Noviembre de ' + str(anno)
    if mes == 12:
        return 'Diciembre de ' + str(anno)
    if mes == 1:
        return 'Enero de ' + str(anno+1)
    if mes == 2:
        return 'Febrero de ' + str(anno+1)
    if mes == 3:
        return 'Marzo de ' + str(anno+1)
    if mes == 4:
        return 'Abril de ' + str(anno+1)
    if mes == 5:
        return 'Mayo de ' + str(anno+1)
    if mes == 6:
        return 'Junio de ' + str(anno+1)
    if mes == 7:
        return 'Julio de ' + str(anno+1)
    if mes == 8:
        return 'Agosto de ' + str(anno+1)


def listarMeses(resultados):
    meses = ''
    for resultado in resultados:
        mes = resultado['month'].month
        meses += str(mes) + ', '

    return meses[:-2]


#Clases para informe para 2 o mas apercibientos
class InformeNumero:
    def __init__(self, alumno, unidad, materia, numero):
        self.alumno = alumno
        self.materia = materia
        self.unidad = unidad
        self.numero = numero

    def __str__(self):
        return self.alumno + " " + self.unidad + " " + self.materia + str(self.numero)

    def __eq__(self, other):
        if not isinstance(other, InformeNumero):
            # don't attempt to compare against unrelated types
            return False

        return self.alumno == other.alumno  and self.unidad == other.unidad and self.materia == other.materia \
               and self.numero == other.numero


#Clases para informe individual para alumnos
class InformeIndividual:
    def __init__(self, alumno, unidad, fecha, apercibimientos):
        self.alumno = alumno
        self.fecha = fecha
        self.unidad = unidad
        self.apercibimientos = apercibimientos

    def __eq__(self, other):
        if not isinstance(other, InformeIndividual):
            # don't attempt to compare against unrelated types
            return False

        return self.alumno == other.alumno and self.unidad == other.unidad and str(self.fecha) == str(other.fecha) \
               and self.apercibimientos == other.apercibimientos


class ApercibimientoIndividual:
    def __init__(self, numero, materia, faltas, porcentaje):
        self.numero = numero
        self.materia = materia
        self.faltas = faltas
        self.porcentaje = porcentaje

    def __eq__(self, other):
        if not isinstance(other, ApercibimientoIndividual):
            # don't attempt to compare against unrelated types
            return False

        return str(self.numero) == str(other.numero) and self.materia == other.materia and str(self.faltas) == str(other.faltas) \
               and str(self.porcentaje) == str(other.porcentaje)


#Clases para informe de resumen de apercibimientps
class InformeResumen:
    def __init__(self, materia, alumnos):
        self.materia = materia
        self.alumnos = alumnos


class InformeResumenCurso:
    def __init__(self, unidad, fecha, materias):
        self.fecha = fecha
        self.unidad = unidad
        self.materias = materias


class ApercibimientoMateria:
    def __init__(self, alumno, numero, meses):
        self.alumno = alumno
        self.numero = numero
        self.meses = meses

