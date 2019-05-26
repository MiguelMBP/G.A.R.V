import os

from celery import shared_task, current_task

from apercibimientos.analisis_pdf import pdf_to_csv


@shared_task
def iterar_pdf(ruta):
    total = contar_pdf(ruta)
    cont = 1
    current_task.update_state(state='PROGRESS',
                              meta={'current': cont, 'total': total, 'percent': 0})
    iterate_pdf(ruta, cont, total)
    return 'completado'


def contar_pdf(ruta):
    pdfCounter = 0
    for root, dirs, files in os.walk(ruta):
        for file in files:
            if file.endswith('.pdf'):
                pdfCounter += 1
    return pdfCounter


def iterate_pdf(extracted, cont, total):
    files = [i for i in os.listdir(extracted) if i.endswith("pdf")]

    for file in files:
        pdf_to_csv(os.path.join(extracted, file))
        cont += 1
        percent = (cont / total) * 100
        current_task.update_state(state='PROGRESS',
                                  meta={'current': cont, 'total': total, 'percent': round(percent, 2), "archivo": file})

    subfolders = [f.path for f in os.scandir(extracted) if f.is_dir()]
    for folder in subfolders:
        cont = iterate_pdf(folder, cont, total)

    return cont
