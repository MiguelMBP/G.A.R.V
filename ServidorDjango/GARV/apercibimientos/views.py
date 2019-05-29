import datetime
import os
import tempfile
from base64 import b64decode

from celery.result import AsyncResult
from django.contrib.admin.views.decorators import staff_member_required
from django.contrib.auth.decorators import login_required
from django.core.files.base import ContentFile
from django.db.models import Count
from django.db.models.functions import TruncMonth
from django.http import HttpResponse, JsonResponse
from django.http import HttpResponseRedirect
from django.shortcuts import render
from django.template.loader import render_to_string
from django.urls import reverse
from django.utils.timezone import now
from weasyprint import HTML

from .extraer_zip import extractZip
from .forms import DocumentForm
from .models import Document, Apercibimiento, AsignaturaEspecial
from .tasks import iterar_pdf, leer_pdf


@login_required
@staff_member_required
def subir_pdf(request):
    """
    Si la petición es POST: Recoge del formulario de la petición el archivo del usuario y llama al método asíncrono de Celery para analizar los documentos y redirige a la misma página GET con el parametro 'job', el id de la tarea
    Si la petición es GET con el parametro 'job': Muestra la barra de progreso del análisis
    Si no es ninguna de las dos, muestra la página para subir un documento
    :param request:
    :return:
    """
    if request.method == 'GET' and 'job' in request.GET:
        job_id = request.GET['job']
        job = AsyncResult(job_id)
        data = job.result or job.state
        context = {
            'data': data,
            'task_id': job_id,
        }
        return render(request, "progreso.html", context)

    elif request.method == 'POST':
        form = DocumentForm(request.POST, request.FILES)
        if form.is_valid():
            newdoc = Document(docfile = request.FILES['docfile'])
            newdoc.save()

            file = request.FILES['docfile'].name

            if file.endswith('.pdf'):
                job = leer_pdf.delay(newdoc.docfile.path)
                return HttpResponseRedirect(reverse('list') + '?job=' + job.id)
            elif file.endswith('.zip'):
                ruta = extractZip(newdoc.docfile.path)
                job = iterar_pdf.delay(ruta)
                return HttpResponseRedirect(reverse('list') + '?job=' + job.id)
            else:
                return HttpResponseRedirect(reverse('list'))

    form = DocumentForm()
    return render(request, 'list.html', {'form': form})


def get_progress(request):
    """
    Recoge el estado de la tarea asincrona con el id pasado por POST y lo envía como JSON
    :param request:
    :return: JSON con la información de la tarea
    """
    if request.is_ajax():
        if 'task_id' in request.POST.keys() and request.POST['task_id']:
            task_id = request.POST['task_id']
            task = AsyncResult(task_id)
            data = task.result or task.state
        else:
            data = 'No task_id in the request'
    else:
        data = 'This is not an ajax request'

    return JsonResponse(data, safe=False)


@login_required
@staff_member_required
def subir_pdf_post(request):
    """
    Recibe un archivo cifrado en base64 y una extensión por POST, descifra el archivo y lo analiza
    :param request:
    :return:
    """
    if request.method == 'POST':
        base64 = request.POST.get('archivo', None)
        extension = request.POST.get('extension', None)
        if base64 and extension:
            newdoc = ContentFile(b64decode(base64), name=str(now) + '.' + extension)
            archivo = Document(docfile=newdoc)
            archivo.save()

            if archivo.docfile.path.endswith('.pdf'):
                job = leer_pdf.delay(archivo.docfile.path)
                return HttpResponse('success ' + job)
            elif archivo.docfile.path.endswith('.zip'):
                ruta = extractZip(archivo.docfile.path)
                job = iterar_pdf.delay(ruta)
                return HttpResponse('success ' + job)

    return HttpResponse('failure')


@login_required
@staff_member_required
def buscarApercibimiento(request):
    """
    Devuelve los periodos académicos de la base de datos
    :param request:
    :return:
    """
    periodo = Apercibimiento.objects.values("periodo_academico").distinct().order_by("periodo_academico")
    return render(request, 'buscar_apercibimiento.html', {'years': periodo})


@login_required
@staff_member_required
def informeApercibimientos(request):
    """
    Renderiza la página de menú de informes
    :param request:
    :return:
    """
    periodo = Apercibimiento.objects.values("periodo_academico").distinct().order_by("periodo_academico")
    return render(request, 'menuInformes.html', {'years': periodo})


@login_required
@staff_member_required
def informeEstadisticas(request):
    """
    Renderiza la página de menú de estadísticas
    :param request:
    :return:
    """
    periodo = Apercibimiento.objects.values("periodo_academico").distinct().order_by("periodo_academico")
    return render(request, 'menuInformeNumeroApercibimientos.html', {'years': periodo})


@login_required
@staff_member_required
def asignaturasEspeciales(request):
    """
    Renderiza la página de asignaturas especiales
    :param request:
    :return:
    """
    lista = AsignaturaEspecial.objects.all()
    return render(request, 'asignaturasEspeciales.html', {'lista': lista})


@login_required
@staff_member_required
def sacarAsignaturas(request):
    """
    Devuelve las asignaturas especiales de la base de datos
    :param request:
    :return:
    """
    if request.is_ajax():
        lista = list(AsignaturaEspecial.objects.values("id", "materia"))
        return JsonResponse(lista, safe=False)
    else:
        return HttpResponse('error')


@login_required
@staff_member_required
def introducirAsignaturas(request):
    """
    Introduce una asignatura especial recibida por POST a la base de datos
    :param request:
    :return:
    """
    if request.GET and request.is_ajax():
        materia = request.GET['materia']
        asignatura = AsignaturaEspecial(materia=materia)
        asignatura.save()
        lista = list(AsignaturaEspecial.objects.values("id", "materia"))
        return JsonResponse(lista, safe=False)
    else:
        return HttpResponse('error')


@login_required
@staff_member_required
def modificarAsignaturas(request):
    """
    Modifica una asignatura especial recibida por POST
    :param request:
    :return:
    """
    if request.GET and request.is_ajax():
        id = request.GET['id']
        materia = request.GET['materia']
        asignatura = AsignaturaEspecial.objects.filter(id=id).first()
        asignatura.materia = materia
        asignatura.save()
        lista = list(AsignaturaEspecial.objects.values("id", "materia"))
        return JsonResponse(lista, safe=False)
    else:
        return HttpResponse('error')


@login_required
@staff_member_required
def eliminarAsignaturas(request):
    """
    Elimina una asignatura recibida por POST
    :param request:
    :return:
    """
    if request.GET and request.is_ajax():
        id = request.GET['id']
        asignatura = AsignaturaEspecial.objects.filter(id=id).delete()
        lista = list(AsignaturaEspecial.objects.values("id", "materia"))
        return JsonResponse(lista, safe=False)
    else:
        return HttpResponse('error')


@login_required
@staff_member_required
def sacarCursos(request):
    """
    Recoge los cursos dentro de un periodo académico
    :param request:
    :return:
    """
    if request.GET and request.is_ajax():
        periodo = request.GET['periodo']
        lista = list(Apercibimiento.objects.values("unidad").distinct().filter(periodo_academico=periodo).order_by('unidad'))
        return JsonResponse(lista, safe=False)
    else:
        return HttpResponse('error')


@login_required
@staff_member_required
def sacarAlumnos(request):
    """
    Recoge los alumnos de un curso en un periodo académico
    :param request:
    :return:
    """
    if request.GET and request.is_ajax():
        periodo = request.GET['periodo']
        curso = request.GET['curso']
        lista = list(Apercibimiento.objects.values("alumno").distinct().filter(periodo_academico=periodo, unidad=curso).order_by('alumno'))
        return JsonResponse(lista, safe=False)
    else:
        return HttpResponse('error')


@login_required
@staff_member_required
def sacarApercibimientos(request):
    """
    Recoge los apercibimientos de un alumno en un periodo académico pasados por GET
    :param request:
    :return:
    """
    if request.GET and request.is_ajax():
        periodo = request.GET['periodo']
        curso = request.GET['curso']
        alumno = request.GET['alumno']
        lista = list(Apercibimiento.objects.values("id", "alumno", "periodo_academico", "unidad", "materia", "fecha_inicio", "fecha_fin", "activo").distinct().filter(periodo_academico=periodo,
                                                                               unidad=curso, alumno=alumno).order_by('fecha_inicio', 'materia'))
        return JsonResponse(lista, safe=False)

    return HttpResponse('error')


@login_required
@staff_member_required
def sacarMeses(request):
    """
    Recoge los meses de un periodo académico
    :param request:
    :return:
    """
    if request.GET and request.is_ajax():
        periodo = request.GET['periodo']
        resultados = list(Apercibimiento.objects.values('fecha_inicio').distinct().filter(periodo_academico=periodo))
        lista = []
        for resultado in resultados:
            lista.append(resultado['fecha_inicio'].month)
        return JsonResponse(lista, safe=False)


@login_required
@staff_member_required
def sacarCursoInformes(request):
    """
    Recoge los cursos de un periodo académico
    :param request:
    :return:
    """
    if request.GET and request.is_ajax():
        periodo = request.GET['periodo']
        mes = request.GET['mes']
        lista = list(Apercibimiento.objects.values('unidad').distinct().filter(periodo_academico=periodo, fecha_inicio__month=mes).order_by('unidad'))

        return JsonResponse(lista, safe=False)


@login_required
@staff_member_required
def actualizarApercibimiento(request):
    """
    Activa o desactiva un apercibimiento
    :param request:
    :return:
    """
    if request.GET and request.is_ajax():
        id = request.GET['id']
        apercibimiento = Apercibimiento.objects.filter(id=id).first()
        apercibimiento.activo = not apercibimiento.activo
        apercibimiento.save()

        periodo = request.GET['periodo']
        curso = request.GET['curso']
        alumno = request.GET['alumno']
        if id and periodo and curso and alumno:
            lista = list(Apercibimiento.objects.values("id", "alumno", "periodo_academico", "unidad", "materia", "fecha_inicio", "fecha_fin", "activo").distinct().filter(periodo_academico=periodo,
                                                                               unidad=curso, alumno=alumno).order_by('fecha_inicio', 'materia'))
            return JsonResponse(lista, safe=False)

    return HttpResponse('error')


@login_required
@staff_member_required
def informeNumeroApercibimiento(request, anno, mes, unidad, minimo):
    """
    Genera el informe de 'Alumnos con N o más apercibimientos' con los datos recibidos de la url
    :param request:
    :param anno: Periodo academico
    :param mes: Mes del informe
    :param unidad: Unidad de la que realizar el informe
    :param minimo: Número mínimo de apercibimientos
    :return:
    """
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
@staff_member_required
def informeApercibimientoIndividual(request, anno, mes, unidad):
    """
    Genera el informe de 'Apercibimientos individuales' con los datos recibidos de la url
    :param request:
    :param anno: Periodo academico
    :param mes: Mes del informe
    :param unidad: Unidad de la que realizar el informe
    :return:
    """
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
@staff_member_required
def informeResumenApercibimiento(request, anno, mes, unidad):
    """
    Genera el informe de 'Apercibimientos por materia' con los datos recibidos de la url
    :param request:
    :param anno: Periodo academico
    :param mes: Mes del informe
    :param unidad: Unidad de la que realizar el informe
    :return:
    """
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
            if len(listamat) > 0:
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


@login_required
@staff_member_required
def estadisticasApercibimientos(request, periodo, inicio, fin):
    """
    Genera el informe de 'Estadísticas de apercibimientos' con los datos recibidos por url
    :param request:
    :param periodo: Periodo académico
    :param inicio: Fecha de inicio del informe
    :param fin: Fecha fin del informe
    :return:
    """
    if 9 <= inicio <= 12:
        fechainicio = datetime.datetime(periodo, inicio, 1)
    else:
        fechainicio = datetime.datetime(periodo+1, inicio, 1)

    if 9 <= fin <= 12:
        fechafin = datetime.datetime(periodo, fin, 28)
    else:
        fechafin = datetime.datetime(periodo+1, fin, 28)

    cursos = Apercibimiento.objects.values('unidad').annotate(count=Count('id')).filter(periodo_academico=periodo, fecha_inicio__range=[fechainicio, fechafin], activo=True).order_by("unidad")

    html_string = render_to_string('ApercibimientosPorUnidad.html', {'lista': cursos, 'inicio': sacarFecha(periodo, inicio), 'fin': sacarFecha(periodo, fin)})
    html = HTML(string=html_string)
    result = html.write_pdf()

    response = HttpResponse(content_type='application/pdf;')
    response['Content-Disposition'] = 'inline; filename=ApercibimientosPorUnidad.pdf'
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

