from django.urls import path

from . import views

urlpatterns = [
    path('createuser/', views.createuser, name="createuser"),
    path('registervisit/', views.registervisit, name="registervisit"),
    path('sendimage/', views.sendimage, name="sendimage"),
    path('changePassword/', views.changePass, name="changePassword"),
    path('resumenVisitas/', views.resumenVisitas, name="resumenVisitas"),
]