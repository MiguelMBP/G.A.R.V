document.getElementById('botonModificarEmpresa').onclick = function () {
    var nombre = $("#nombre").val();
    var cif = $("#cif").val();
    var direccion = $("#direccion").val();
    var poblacion = $("#poblacion").val();
    var latitud = $("#latitud").val();
    var longitud = $("#longitud").val();
    var distancia = $("#distancia").val();


    if (nombre != "" && cif != "" && direccion != "" && poblacion != "" && latitud != "" && longitud != "" && distancia != "") {
        if ($.isNumeric(latitud) && $.isNumeric(longitud) && $.isNumeric(distancia)) {
            var url = $(location).attr('href'),
            parts = url.split("/"),
            id = parts[parts.length-2];

            url = '/visitas/editarEmpresa/' + id + '/'
            var formData = {
                'nombre': nombre, 'cif': cif, 'direccion': direccion, 'poblacion': poblacion, 'latitud': latitud, "longitud": longitud, 'distancia': distancia
            };
            console.log(formData)
             $.ajaxSetup({
                headers: { "X-CSRFToken": getCookie("csrftoken") }
             });
            $.ajax({
                url: url,
                type: "post",
                data: formData,
                success: function(respuesta) {
                    if (respuesta == 'Duplicate cif') {
                        alert("El CIF ya existe")
                    } else if (respuesta == 'error'){
                        alert('Error modificando empresa')
                    } else {
                        window.location = '/visitas/empresas'
                    }
                },
                error: function() {
                    alert("Error al modificar la empresa");
                }
            });
        } else {
            alert("La latitud, longitud y distancia deben ser números")
        }

    } else {
        alert("No pueden haber campos vacios")
    }
}

function getCookie(c_name)
{
    if (document.cookie.length > 0)
    {
        c_start = document.cookie.indexOf(c_name + "=");
        if (c_start != -1)
        {
            c_start = c_start + c_name.length + 1;
            c_end = document.cookie.indexOf(";", c_start);
            if (c_end == -1) c_end = document.cookie.length;
            return unescape(document.cookie.substring(c_start,c_end));
        }
    }
    return "";
 }