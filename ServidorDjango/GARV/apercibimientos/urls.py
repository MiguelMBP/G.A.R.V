from django.urls import path

from . import views

urlpatterns = [
    path('list/', views.subir_pdf, name='list'),
    path('list/', views.subir_pdf, name='subir-pdf'),
    path('login/', views.a_login, name="login"),
    path('buscar/', views.buscarApercibimiento, name="buscarApercibimientos"),
    path('informe/', views.informeApercibimientos, name="informeApercibimientos"),
    path('asignaturas/', views.asignaturasEspeciales, name="asignaturas"),
    path('getCursos/', views.sacarCursos, name="getCursos"),
    path('getAlumnos/', views.sacarAlumnos, name="getAlumnos"),
    path('getApercibimientos/', views.sacarApercibimientos, name="getApercibimientos"),
    path('getMeses/', views.sacarMeses, name="getMeses"),
    path('getCursosInformes/', views.sacarCursoInformes, name="getCursos"),
    path('updateApercibimiento/', views.actualizarApercibimiento, name="updateApercibimientos"),
    path('getAsignaturas/', views.sacarAsignaturas, name="getAsignaturas"),
    path('anadirAsignatura/', views.añadirAsignaturas, name="anadirAsignatura"),
    path('modificarAsignatura/', views.modificarAsignaturas, name="modificarAsignatura"),
    path('eliminarAsignatura/', views.eliminarAsignaturas, name="eliminarAsignatura"),
    path('informe/informeNumeroApercibimiento/<int:anno>/<int:mes>/<str:unidad>/<int:minimo>', views.informeNumeroApercibimiento, name="informeNumeroApercibimiento"),
    path('informe/informeApercibimientoIndividual/<int:anno>/<int:mes>/<str:unidad>', views.informeApercibimientoIndividual, name="informeApercibimientoIndividual"),
    path('informe/informeResumenApercibimiento/<int:anno>/<int:mes>/<str:unidad>', views.informeResumenApercibimiento, name="informeResumenApercibimiento"),
    path('estadisticasApercibimiento/<int:periodo>/<int:inicio>/<int:fin>', views.estadisticasApercibimientos, name="estadisticasApercibimiento"),
]