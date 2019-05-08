$( document ).ready(function() {
    $('#tipoSelect').val("");
    $('#periodoSelect').val("");
    $("#descripcion").text("");
    $('#minimoApercibimientos').prop('disabled', 'disabled');
    $('#alumnoSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Alumno</option>');
    $('#mesSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Mes</option>');
    $('#cursoSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Curso</option>');
});

$('input[type=radio][name=cursos]').change(function() {
    if (this.value == 'todosCursos') {
        $('#cursoSelect').prop('disabled', 'disabled');
        $('#cursoSelect').val("");
    }
    else if (this.value == 'porCurso') {
        $('#cursoSelect').prop('disabled', false);
    }
});

document.getElementById('informes').periodoSelect.onchange = function () {
    var periodo = $("#periodoSelect").val(),
    url = '/apercibimientos/getMeses'
    $.ajax({
        url: url + '?periodo=' + periodo,
        success: function(respuesta) {
            $('#mesSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Mes</option>');

            for (var i = 0, len = respuesta.length; i<len; i++) {
                var unidad = respuesta[i];
                var nombre = unidad['month']
                $('#mesSelect').append(new Option(nombre, numeroAMes(nombre)));
            }

        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });
}

document.getElementById('informes').mesSelect.onchange = function () {
    var periodo = $("#periodoSelect").val(),
    mes = $("#mesSelect").val()
    url = '/apercibimientos/getCursosInformes'
    $.ajax({
        url: url + '?periodo=' + periodo + '&mes=' + 11,
        success: function(respuesta) {
            $('#cursoSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Curso</option>');
            for (var i = 0, len = respuesta.length; i<len; i++) {
                var curso = respuesta[i];
                var nombre = curso['unidad']
                $('#cursoSelect').append(new Option(nombre, nombre));
            }

        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });
}

document.getElementById('informes').tipoSelect.onchange = function () {
    var tipo = $("#tipoSelect").val();

    if (tipo == 1) {
        $('#minimoApercibimientos').prop('disabled', 'disabled');
        $("#descripcion").text("Este informe genera un listado con los alumnos apercibidos por materia y el número de apercibientos desde la fecha introducida");
    } else if (tipo == 2) {
        $('#minimoApercibimientos').prop('disabled', false);
        $("#descripcion").text("Este informe genera un pdf con el listado de los alumnos con el número de apercibimientos especificados como fecha máxima la indicada en el formulario");
    } else if (tipo == 3) {
        $('#minimoApercibimientos').prop('disabled', 'disabled');
        $("#descripcion").text("Este informe genera un pdf con apercibimientos individuales por alumno");
    }

}

document.getElementById('botonInformes').onclick = function () {
    var periodo = $("#periodoSelect").val(),
    curso = $("#cursoSelect").val(),
    mes = $("#mesSelect").val(),
    tipo = $("#tipoSelect").val();

    if (tipo == 1) {
        window.location.replace("informeResumenApercibimiento/" + periodo + "/" + mes + "/" + curso);

    } else if (tipo == 2) {
        minimo = $("#minimoApercibimientos").val()
        window.location.replace("informeNumeroApercibimiento" + periodo + "/" + mes + "/" + curso + "/" + minimo);

    } else if (tipo == 3) {
        window.location.replace("informeApercibimientoIndividual" + periodo + "/" + mes + "/" + curso);

    }
    url = '/apercibimientos/getApercibimientos'
    $.ajax({
        url: url + '?periodo=' + periodo + '&curso=' + curso + '&alumno=' + alumno,
        success: function(respuesta) {
           rellenarTabla(respuesta)
        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });
}



function numeroAMes(mes) {
    var month_name = new Array(12);
    month_name[0] = "Enero";
    month_name[1] = "Febrero";
    month_name[2] = "Marzo";
    month_name[3] = "Abril";
    month_name[4] = "Mayo";
    month_name[5] = "Junio";
    month_name[6] = "Julio";
    month_name[7] = "Agosto";
    month_name[8] = "Septiembre";
    month_name[9] = "Octubre";
    month_name[10] = "Noviembre";
    month_name[11] = "Deciembre";

    console.log(month_name[mes])
    return month_name[mes]
    //return mes
}


