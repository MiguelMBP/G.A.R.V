from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.paginator import Paginator
from django.shortcuts import render

from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.urls import reverse

from .models import Document, Apercibimiento
from .forms import DocumentForm
from .extraer_zip import extractZip
from .analisis_pdf import pdf_to_csv


def index(request):
    return HttpResponse("Hello there!")


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


def buscarApercibimiento(request):
    unidad = request.GET.get('unidad')
    alumno = request.GET.get('alumno')
    apercibimientos = Apercibimiento.objects.filter(alumno=alumno, unidad=unidad)
    return render(request, 'buscar_apercibimiento.html', {'apercibimientos': apercibimientos})