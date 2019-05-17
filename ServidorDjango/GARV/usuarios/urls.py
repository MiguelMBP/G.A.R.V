from django.urls import path

from . import views

urlpatterns = [
    path('createuser/', views.createuser, name="createuser"),
    path('changePassword/', views.changePass, name="changePassword"),
    path('usuarios/', views.usuarios, name="usuarios"),
    path('editarUsuario/<int:id>/', views.editarUsuario, name="editarUsuario"),
    path('crearUsuario/', views.crearUsuario, name="crearUsuario"),
    path('updateuser/', views.updateuser, name="updateuser"),
    path('login/', views.a_login, name="a_login"),
    path('importarUsuarios/', views.importarusuarios, name="importarUsuarios"),
]