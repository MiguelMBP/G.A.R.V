from django.urls import path

from . import views

urlpatterns = [
    path('', views.index, name='index-apercibimientos'),
    path('list/', views.subir_pdf, name='list'),
    path('list/', views.subir_pdf, name='subir-pdf'),
    path('login/', views.a_login, name="login"),
    path('apercibimientos/', views.mostrarApercibimientos, name="apercibimientos"),
    path('buscar/', views.buscarApercibimiento, name="buscarApercibimientos"),
]