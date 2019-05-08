from django.urls import path

from . import views

urlpatterns = [
    path('list/', views.subir_pdf, name='list'),
    path('list/', views.subir_pdf, name='subir-pdf'),
    path('login/', views.a_login, name="login"),
    path('buscar/', views.buscarApercibimiento, name="buscarApercibimientos"),
    path('getCursos/', views.sacarCursos, name="getCursos"),
    path('getAlumnos/', views.sacarAlumnos, name="getAlumnos"),
    path('getApercibimientos/', views.sacarApercibimientos, name="getApercibimientos"),
    path('updateApercibimiento/', views.actualizarApercibimiento, name="updateApercibimientos"),
    path('informe/informeNumeroApercibimiento/<int:anno>/<int:mes>/<str:unidad>/<int:minimo>', views.informeNumeroApercibimiento, name="informeNumeroApercibimiento"),
    path('informe/informeApercibimientoIndividual/<int:anno>/<int:mes>/<str:unidad>', views.informeApercibimientoIndividual, name="informeApercibimientoIndividual"),
    path('informe/informeResumenApercibimiento/<int:anno>/<int:mes>/<str:unidad>', views.informeResumenApercibimiento, name="informeResumenApercibimiento"),
]