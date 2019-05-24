document.getElementById('botonEditarUsuario').onclick = function () {
    var contrasena = $("#contraseña").val();
    var contrasena2 = $("#contraseña2").val();
    var usuario = $("#usuario").val();
    var nombre = $("#nombre").val();
    var apellidos = $("#apellidos").val();
    var correo = $("#correo").val();
    var dni = $("#dni").val();
    var tutor = $("#tutor").val();

    if (contrasena != "" && usuario != "") {
        if (contrasena == contrasena2) {
            url = '/usuarios/changePassword/'
            var formData = {
                'username': usuario, 'password': contrasena
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
                    alert("Error al modificar la contraseña");
                }
            });
        } else {
            alert("Las contraseñas no coinciden");
        }

    }

    if (usuario != "" && nombre != "" && apellidos != "" && correo != "" && dni != "") {

            url = '/usuarios/updateuser/'
            var formData = {
                'username': usuario, 'nombre': nombre, 'email': correo, 'apellidos': apellidos, "dni": dni, 'curso': tutor
            };
             $.ajaxSetup({
                headers: { "X-CSRFToken": getCookie("csrftoken") }
             });
            $.ajax({
                url: url,
                type: "post",
                data: formData,
                success: function(respuesta) {
                    if (respuesta == 'dni repetido') {
                        alert("Ya existe un usuario con ese DNI")
                    } else {
                        window.location = '/usuarios/usuarios'
                    }
                },
                error: function() {
                    alert("Error al modificar el usuario");
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