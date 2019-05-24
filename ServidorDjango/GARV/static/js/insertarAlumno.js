document.getElementById('botonCrearAlumno').onclick = function () {
    var nombre = $("#nombre").val();
    var dni = $("#dni").val();
    var apellidos = $("#apellidos").val();
    var curso = $("#curso").val();
    var empresa = $("#empresaSelect").val();

    if (nombre != "" && dni != "" && apellidos != "" && curso != "" && empresa != "") {
        url = '/visitas/crearAlumno/'
        var formData = {
            'nombre': nombre, 'dni': dni, 'apellidos': apellidos, 'curso': curso, 'empresa': empresa
        };
         $.ajaxSetup({
            headers: { "X-CSRFToken": getCookie("csrftoken") }
         });
        $.ajax({
            url: url,
            type: "post",
            data: formData,
            success: function(respuesta) {
                if (respuesta == 'Duplicate dni') {
                    alert("El DNI ya existe")
                } else if (respuesta == 'error'){
                    alert('Error introduciendo alumno')
                } else {
                    window.location = '/visitas/alumnos'
                }
            },
            error: function() {
                alert("Error al introducir la empresa");
            }
        });
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