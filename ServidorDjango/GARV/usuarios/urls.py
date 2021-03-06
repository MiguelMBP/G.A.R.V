from django.urls import path

from . import views

urlpatterns = [
    path('createuser/', views.createuser, name="createuser"),
    path('changePassword/', views.changePass, name="changePassword"),
    path('usuarios/', views.usuarios, name="usuarios"),
    path('editarUsuario/<int:id>/', views.editarUsuario, name="editarUsuario"),
    path('crearUsuario/', views.crearUsuario, name="crearUsuario"),
    path('updateuser/', views.updateuser, name="updateuser"),
    path('eliminarUsuarios/', views.eliminarusuarios, name="eliminarusuarios"),
    path('login/', views.a_login, name="a_login"),
    path('importarUsuarios/', views.importarusuarios, name="importarUsuarios"),
    path('importarPost/', views.importarPost, name="importarPost"),
    path('isStaff/', views.is_staff, name="isStaff"),
]