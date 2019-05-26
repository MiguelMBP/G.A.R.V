$( document ).ready(function() {
    $('#periodoSelect').val("");
    $('#mesInicioSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Mes</option>');
    $('#mesFinSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Mes</option>');
});

document.getElementById('informes').periodoSelect.onchange = function () {
    var periodo = $("#periodoSelect").val(),
    url = '/apercibimientos/getMeses'
    $.ajax({
        url: url + '?periodo=' + periodo,
        success: function(respuesta) {
            $('#mesInicioSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Mes</option>');
            $('#mesFinSelect').find('option').remove().end().append('<option value="" selected disabled hidden>Elegir Mes</option>');

            for (var i = 0, len = respuesta.length; i<len; i++) {
                var mes = respuesta[i];
                $('#mesInicioSelect').append(new Option(numeroAMes(mes), mes));
                $('#mesFinSelect').append(new Option(numeroAMes(mes), mes));
            }

        },
        error: function() {
            console.log("No se ha podido obtener la información");
        }
    });
}

document.getElementById('botonInformes').onclick = function () {
    var periodo = $("#periodoSelect").val(),
    mesInicio = $("#mesInicioSelect").val(),
    mesFin = $("#mesFinSelect").val();


    if (periodo != null && mesInicio != null && mesFin != null) {
        window.open("/apercibimientos/informe/estadisticasApercibimiento/" + periodo + "/" + mesInicio + "/" + mesFin);
    }


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
    month_name[11] = "Diciembre";

    return month_name[mes-1]
    //return mes
}