from django.contrib.auth.decorators import login_required
from django.shortcuts import render

from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.urls import reverse


from .models import Document
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


def createuser(request):
    if request.method == "POST":
        text = request.POST.get("clave")
        return HttpResponse('success', text)
