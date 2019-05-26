import zipfile
import os


def extractZip(filepath):
    zip_ref = zipfile.ZipFile(filepath, 'r')

    extracted = os.path.join(os.path.splitext(filepath)[0])
    os.mkdir(extracted)
    extracted = os.path.join(os.path.splitext(filepath)[0], "extracted")
    os.mkdir(extracted)

    zip_ref.extractall(extracted)
    zip_ref.close()

    return (extracted)
