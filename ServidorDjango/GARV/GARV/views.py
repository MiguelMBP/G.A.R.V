from django.contrib.admin.views.decorators import staff_member_required
from django.contrib.auth.decorators import login_required
from django.shortcuts import render


@login_required
@staff_member_required
def index(request):
    return render(request, 'index.html')