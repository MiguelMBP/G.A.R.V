# Generated by Django 2.2 on 2019-04-20 10:56

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Alumno',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('dni', models.CharField(max_length=9, unique=True)),
                ('nombre', models.CharField(max_length=100)),
                ('apellidos', models.CharField(max_length=100)),
                ('curso', models.CharField(max_length=100)),
            ],
        ),
        migrations.CreateModel(
            name='Empresa',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('cif', models.CharField(max_length=9, unique=True)),
                ('nombre', models.CharField(max_length=100)),
                ('poblacion', models.CharField(max_length=100)),
                ('direccion', models.CharField(max_length=100)),
                ('coordenadaX', models.FloatField()),
                ('coordenadaY', models.FloatField()),
            ],
        ),
        migrations.CreateModel(
            name='Profesor',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('dni', models.CharField(max_length=9, unique=True)),
                ('nombre', models.CharField(max_length=100)),
                ('apellidos', models.CharField(max_length=100)),
                ('cursoTutor', models.CharField(max_length=100, null=True)),
                ('usuario', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='Visita',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('fecha', models.DateField()),
                ('distancia', models.FloatField()),
                ('imagen', models.ImageField(upload_to='')),
                ('alumno', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='visitas.Alumno')),
                ('profesor', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='visitas.Profesor')),
            ],
        ),
        migrations.AddField(
            model_name='alumno',
            name='empresa',
            field=models.ForeignKey(default='-1', on_delete=django.db.models.deletion.CASCADE, to='visitas.Empresa'),
        ),
    ]
