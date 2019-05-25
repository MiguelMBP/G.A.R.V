import os

from celery import shared_task, current_task

from apercibimientos.analisis_pdf import pdf_to_csv


@shared_task
def iterar_pdf(ruta):
    files = [i for i in os.listdir(ruta) if i.endswith("pdf")]
    cont = 1
    current_task.update_state(state='PROGRESS',
                              meta={'current': cont, 'total': len(files), 'percent': 0})
    for file in files:
        pdf_to_csv(os.path.join(ruta, file))
        cont += 1
        percent = (cont/len(files)) * 100
        current_task.update_state(state='PROGRESS',
                                  meta={'current': cont, 'total': len(files), 'percent': round(percent, 2)})

    # subfolders = [f.path for f in os.scandir(ruta) if f.is_dir()]
    # for folder in subfolders:
    #     # iterate_pdf(folder)
    #     print()
    return 'completado'

@shared_task
def add(x, y):
    return x + y