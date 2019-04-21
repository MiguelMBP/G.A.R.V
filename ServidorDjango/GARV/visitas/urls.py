from django.urls import path

from . import views

urlpatterns = [
    path('createuser/', views.createuser, name="createuser")
]