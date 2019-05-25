import zipfile
import os
from .analisis_pdf import pdf_to_csv


def extractZip(filepath):
    zip_ref = zipfile.ZipFile(filepath, 'r')

    extracted = os.path.join(os.path.splitext(filepath)[0])
    os.mkdir(extracted)
    extracted = os.path.join(os.path.splitext(filepath)[0], "extracted")
    os.mkdir(extracted)

    zip_ref.extractall(extracted)
    zip_ref.close()

    return (extracted)
    #iterate_pdf(extracted)


def iterate_pdf(extracted):
    files = [i for i in os.listdir(extracted) if i.endswith("pdf")]

    for file in files:
        pdf_to_csv(os.path.join(extracted, file))

    subfolders = [f.path for f in os.scandir(extracted) if f.is_dir()]
    for folder in subfolders:
        iterate_pdf(folder)
