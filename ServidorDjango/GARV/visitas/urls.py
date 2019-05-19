from django.conf import settings
from django.conf.urls.static import static
from django.urls import path

from . import views

urlpatterns = [
    path('registervisit/', views.registervisit, name="registervisit"),
    path('sendimage/', views.sendimage, name="sendimage"),
    path('resumenVisitas/', views.resumenVisitas, name="resumenVisitas"),
    path('visitas/', views.visitas, name="visitas"),
    path('getVisitas/', views.getVisitas, name="getVisitas"),
    path('actualizarVisita/', views.actualizarVisita, name="actualizarVisita"),
    path('verImagen/', views.verImagen, name="verImagen"),
    path('alumnos/', views.alumnos, name="alumnos"),
    path('empresas/', views.empresas, name="empresas"),
    path('crearAlumno/', views.crearAlumno, name="crearAlumno"),
    path('crearEmpresa/', views.crearEmpresa, name="crearEmpresa"),
    path('editarAlumno/<int:id>/', views.editarAlumno, name="editarAlumno"),
    path('editarEmpresa/<int:id>/', views.editarEmpresa, name="editarEmpresa"),
]
