# Generated by Django 2.2 on 2019-05-12 09:00

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('visitas', '0004_auto_20190427_1003'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='profesor',
            name='apellidos',
        ),
        migrations.RemoveField(
            model_name='profesor',
            name='nombre',
        ),
        migrations.AlterField(
            model_name='visita',
            name='imagen',
            field=models.ImageField(upload_to='images/%Y/%m/%d'),
        ),
    ]
