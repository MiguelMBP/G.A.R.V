document.getElementById('botonEliminarUsuario').onclick = function () {
    var ids = [];
     $('#tablaUsuarios').find('input[type="checkbox"]:checked').each(function () {
       ids.push(this.id.split('_')[1])
    });
    if (ids.length > 0) {
        url = '/usuarios/eliminarUsuarios/'
        var formData = {
            'id': ids
        };
         $.ajaxSetup({
            headers: { "X-CSRFToken": getCookie("csrftoken") }
         });
        $.ajax({
            url: url,
            type: "post",
            data: formData,
            success: function(respuesta) {

            },
            error: function() {
                alert("Error al eliminar usuario(s)");
            }
        });


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