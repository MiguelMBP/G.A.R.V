from django.urls import path

from . import views

urlpatterns = [
    path('list/', views.subir_pdf, name='list'),
    path('list/', views.subir_pdf, name='subir-pdf'),
    path('login/', views.a_login, name="login"),
    path('apercibimientos/', views.mostrarApercibimientos, name="apercibimientos"),
    path('buscar/', views.buscarApercibimiento, name="buscarApercibimientos"),
    path('informe/informeNumeroApercibimiento/<int:anno>/<int:mes>/<str:unidad>/<int:minimo>', views.informeNumeroApercibimiento, name="informeNumeroApercibimiento"),
    path('informe/informeApercibimientoIndividual/<int:anno>/<int:mes>/<str:unidad>', views.informeApercibimientoIndividual, name="informeApercibimientoIndividual"),
    path('informe/informeResumenApercibimiento/<int:anno>/<int:mes>/<str:unidad>', views.informeResumenApercibimiento, name="informeResumenApercibimiento"),
]