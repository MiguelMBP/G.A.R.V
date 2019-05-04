import datetime
import tempfile

from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from django.core.paginator import Paginator
from django.shortcuts import render

from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.template.loader import render_to_string
from django.urls import reverse
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
    unidad = request.GET.get('unidad')
    alumno = request.GET.get('alumno')
    apercibimientos = Apercibimiento.objects.filter(alumno=alumno, unidad=unidad)
    return render(request, 'buscar_apercibimiento.html', {'apercibimientos': apercibimientos})


@login_required
def informeNumeroApercibimiento(request, anno, mes, unidad, minimo):
    if 9 <= mes <= 12:
        fecha = datetime.datetime(anno, mes, 31)
    else:
        fecha = datetime.datetime(anno+1, mes, 31)

    fechacurso = datetime.datetime(anno, 9, 1)

    apercibimientos = Apercibimiento.objects.only("alumno", "unidad", "materia", "fecha_inicio").filter(periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha], unidad=unidad).order_by("unidad", "alumno", "materia")
    lista = []
    for apercibimiento in apercibimientos:
        resultado = Apercibimiento.objects.filter(alumno=apercibimiento.alumno, unidad=apercibimiento.unidad, materia=apercibimiento.materia, periodo_academico=anno, fecha_inicio__range=[fechacurso, fecha]).count()

        if resultado >= minimo:
            apercibimiento = InformeNumero(alumno=apercibimiento.alumno, unidad=apercibimiento.unidad, materia=apercibimiento.materia, numero=resultado)
            if apercibimiento not in lista:
                lista.append(apercibimiento)

    html_string = render_to_string('informeNumeroApercibimiento.html', {'apercibimientos': lista})
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


