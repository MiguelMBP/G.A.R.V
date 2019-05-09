$( document ).ready(function() {
    url = '/apercibimientos/getAsignaturas'
    $.ajax({
        url: url,
        success: function(respuesta) {
            $('#lista').html("");

            for (var i = 0, len = respuesta.length; i<len; i++) {
                var asignatura = respuesta[i];
                $('#lista').append(divs(asignatura));
            }

        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });
});

document.getElementById('add_asignatura').onclick = function () {
    var asignatura = $("#add").val();

    if (asignatura != null && asignatura != "" && asignatura != " ") {
        url = '/apercibimientos/anadirAsignatura'
        $.ajax({
            url: url + '?materia=' + asignatura,
            success: function(respuesta) {
                $('#lista').html("");

                for (var i = 0, len = respuesta.length; i<len; i++) {
                    var asignatura = respuesta[i];
                    $('#lista').append(divs(asignatura));
                }

            },
            error: function() {
                console.log("No se ha podido obtener la información");
            }
        });
    }

}

function modificar(boton) {
    var id = boton.id.split('_')[1];
    var asignatura = $("#materia" + id).val();
    if (asignatura != null && asignatura != "" && asignatura != " ") {
        url = '/apercibimientos/modificarAsignatura'
        $.ajax({
            url: url + '?materia=' + asignatura + '&id=' + id,
            success: function(respuesta) {
                $('#lista').html("");

                for (var i = 0, len = respuesta.length; i<len; i++) {
                    var asignatura = respuesta[i];
                    $('#lista').append(divs(asignatura));
                }

            },
            error: function() {
                console.log("No se ha podido obtener la información");
            }
        });
    }
}

function eliminar(boton) {
    var id = boton.id.split('_')[1];

    url = '/apercibimientos/eliminarAsignatura'
    $.ajax({
        url: url + '?id=' + id,
        success: function(respuesta) {
            $('#lista').html("");

            for (var i = 0, len = respuesta.length; i<len; i++) {
                var asignatura = respuesta[i];
                $('#lista').append(divs(asignatura));
            }

        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });

}

function divs(i) {
    var divs = '<div class="input-group">'+
                '<input type="text" class="form-control" placeholder="Nombre de la asignatura" aria-label="Nombre de la asignatura"'+
                   'aria-describedby="basic-addon2" value="' + i.materia + '" id="materia' + i.id + '">'+
                '<div class="input-group-append">'+
                    '<button class="btn btn-outline-secondary" type="button" onclick="modificar(this)" id="modificar_' + i.id + '">Modificar</button>'+
                    '<button class="btn btn-outline-secondary" type="button" id="eliminar_' + i.id + '" onclick="eliminar(this)">Eliminar</button>'+
                '</div></div>';
    return divs
}

