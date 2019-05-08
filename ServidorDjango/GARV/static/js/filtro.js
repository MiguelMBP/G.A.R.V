$( document ).ready(function() {
    $('#periodoSelect').val("");
     $('#alumnoSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Alumno</option>');
     $('#cursoSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Curso</option>');
});

document.getElementById('filtro').periodoSelect.onchange = function () {
    var periodo = $("#periodoSelect").val(),
    url = '/apercibimientos/getCursos'
    $.ajax({
        url: url + '?periodo=' + periodo,
        success: function(respuesta) {
            $('#cursoSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Curso</option>');

            for (var i = 0, len = respuesta.length; i<len; i++) {
                var unidad = respuesta[i];
                var nombre = unidad['unidad']
                $('#cursoSelect').append(new Option(nombre, nombre));
            }

        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });
}

document.getElementById('filtro').cursoSelect.onchange = function () {
    var periodo = $("#periodoSelect").val(),
    curso = $("#cursoSelect").val()
    url = '/apercibimientos/getAlumnos'
    $.ajax({
        url: url + '?periodo=' + periodo + '&curso=' + curso,
        success: function(respuesta) {
            $('#alumnoSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Alumno</option>');
            for (var i = 0, len = respuesta.length; i<len; i++) {
                var alumno = respuesta[i];
                var nombre = alumno['alumno']
                $('#alumnoSelect').append(new Option(nombre, nombre));
            }

        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });
}

document.getElementById('botonFiltrarApercibimiento').onclick = function () {
    var periodo = $("#periodoSelect").val(),
    curso = $("#cursoSelect").val(),
    alumno = $("#alumnoSelect").val();

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

function rellenarTabla(respuesta) {
    var table = $("#tabla_apercibimientos tbody");
    table.empty();

    for (var i = 0, len = respuesta.length; i<len; i++) {
        var a = respuesta[i];
        var estado = 'Activo';
        var activo = a['activo']
        var checked = checked="checked"

        if (!a['activo']) {
            estado = 'Innactivo';
            checked = ''
        }

        table.append("<tr><td>"+a['alumno']+"</td><td>"+a['periodo_academico']+"</td><td>"+a['unidad']+"</td>><td>"+a['materia']+"</td>><td>"+a['fecha_inicio']+
        "</td>><td>"+a['fecha_fin']+"</td><td>"+estado+"</td><td><input type='checkbox' onclick='desActivarApercibimiento(this)' " + checked + " id='chk_"+a['id']+"' </td> </tr>");
    }
}

function desActivarApercibimiento(chk_activo) {
    var id = chk_activo.id.split('_')[1];
    var periodo = $("#periodoSelect").val(),
    curso = $("#cursoSelect").val(),
    alumno = $("#alumnoSelect").val();

    url = '/apercibimientos/updateApercibimiento'
    $.ajax({
        url: url + '?periodo=' + periodo + '&curso=' + curso + '&alumno=' + alumno + '&id=' + id,
        success: function(respuesta) {
           rellenarTabla(respuesta)
        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });

}
