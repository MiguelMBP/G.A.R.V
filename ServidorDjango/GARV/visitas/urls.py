from django.conf import settings
from django.conf.urls.static import static
from django.urls import path

from . import views

urlpatterns = [
    path('createuser/', views.createuser, name="createuser"),
    path('registervisit/', views.registervisit, name="registervisit"),
    path('sendimage/', views.sendimage, name="sendimage"),
    path('changePassword/', views.changePass, name="changePassword"),
    path('resumenVisitas/', views.resumenVisitas, name="resumenVisitas"),
    path('visitas/', views.visitas, name="visitas"),
    path('getVisitas/', views.getVisitas, name="getVisitas"),
    path('actualizarVisita/', views.actualizarVisita, name="actualizarVisita"),
    path('verImagen/', views.verImagen, name="verImagen"),
    path('usuarios/', views.usuarios, name="usuarios"),
    path('editarUsuario/<int:id>/', views.editarUsuario, name="editarUsuario"),
    path('crearUsuario/', views.crearUsuario, name="crearUsuario"),
]