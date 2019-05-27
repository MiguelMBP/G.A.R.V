import zipfile
import os


def extractZip(filepath):
    """
    Extrae el fichero zip
    :param filepath: Ruta al fichero
    :return: ruta a la carpeta extraida
    """
    zip_ref = zipfile.ZipFile(filepath, 'r')

    extracted = os.path.join(os.path.splitext(filepath)[0])
    os.mkdir(extracted)
    extracted = os.path.join(os.path.splitext(filepath)[0], "extracted")
    os.mkdir(extracted)

    zip_ref.extractall(extracted)
    zip_ref.close()

    return (extracted)
