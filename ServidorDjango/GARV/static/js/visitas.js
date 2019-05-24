$( document ).ready(function() {
    $('#profesorSelect').val("");
});

function rellenarTabla(respuesta) {
    var table = $("#tabla_visitas tbody");
    table.empty();

    for (var i = 0, len = respuesta.length; i<len; i++) {
        var a = respuesta[i];
        var estado = 'Validada';
        var activo = a['validada']
        var checked = checked="checked"

        if (!a['validada']) {
            estado = 'Invalida';
            checked = ''
        }

        table.append("<tr><td>"+a['profesor'] + "</td><td>"+a['alumno'] + "</td><td>"+a['empresa']+"</td>><td>"+a['fecha']+"</td>><td><a href='/visitas/verImagen?id=" + a['id'] + "'>Imagen</a>"+
        "</td><td>"+estado+"</td><td><input type='checkbox' onclick='inValidar(this)' " + checked + " id='chk_"+a['id']+"' </td> </tr>");
    }
}

function inValidar(chk_activo) {
    var id = chk_activo.id.split('_')[1];
    var profesor = $("#profesorSelect").val();

    url = '/visitas/actualizarVisita'
    $.ajax({
        url: url + '?profesor=' + profesor + '&visita=' + id,
        success: function(respuesta) {
           rellenarTabla(respuesta)
        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });

}

document.getElementById('botonInformeVisitas').onclick = function () {
    var importe = parseFloat($("#importe").val());

    if(!isNaN(importe)){
        url = '/visitas/resumenVisitas?valor=' + importe
        window.location.replace(url);
    }
}

document.getElementById('botonFiltrarVisitas').onclick = function () {
    var profesor = $("#profesorSelect").val();

    url = '/visitas/getVisitas'
    $.ajax({
        url: url + '?profesor=' + profesor,
        success: function(respuesta) {
           rellenarTabla(respuesta)
        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });
}