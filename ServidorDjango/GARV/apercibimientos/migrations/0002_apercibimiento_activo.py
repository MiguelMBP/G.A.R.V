# Generated by Django 2.2 on 2019-04-13 13:37

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('apercibimientos', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='apercibimiento',
            name='activo',
            field=models.BooleanField(default=True),
        ),
    ]