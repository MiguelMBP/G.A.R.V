import os

from celery import shared_task, current_task

from apercibimientos.analisis_pdf import pdf_to_csv


@shared_task
def iterar_pdf(ruta):
    """
    Método asincrono ejecutado por celery
    Cuenta el número de archivos pdf de en el directorio, actualiza el estado de la tarea y empreza a analizar los documentos
    :param ruta: ruta al directorio
    :return: Estado completado de la tarea
    """
    total = contar_pdf(ruta)
    cont = 1
    current_task.update_state(state='PROGRESS',
                              meta={'current': cont, 'total': total, 'percent': 0})
    iterate_pdf(ruta, cont, total)
    return 'completado'


@shared_task
def leer_pdf(ruta):
    """
    Método asincrono ejecutado por celery
    Lee un solo archivo pdf
    :param ruta: ruta al directorio
    :return: Estado completado de la tarea
    """
    current_task.update_state(state='PROGRESS',
                              meta={'current': 1, 'total': 1, 'percent': 100})
    pdf_to_csv(ruta)
    return 'completado'

def contar_pdf(ruta):
    """
    Cuenta el número de archivos pdf en el directorio
    :param ruta: ruta al directorio
    :return: número de documentos
    """
    pdfCounter = 0
    for root, dirs, files in os.walk(ruta):
        for file in files:
            if file.endswith('.pdf'):
                pdfCounter += 1
    return pdfCounter


def iterate_pdf(extracted, cont, total):
    """
    Método recursivo que llama al método pdf_to_csv de analisis_pdf por cada archivo pdf y actualiza el estado de la tarea
    :param extracted:
    :param cont:
    :param total:
    :return:
    """
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
