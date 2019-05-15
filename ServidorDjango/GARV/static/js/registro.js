document.getElementById('botonCrearUsuario').onclick = function () {
    var contrasena = $("#contraseña").val();
    var contrasena2 = $("#contraseña2").val();
    var usuario = $("#usuario").val();
    var nombre = $("#nombre").val();
    var apellidos = $("#apellidos").val();
    var correo = $("#correo").val();
    var dni = $("#dni").val();
    var tutor = $("#tutor").val();


    if (contrasena != "" && usuario != "" && nombre != "" && apellidos != "" && correo != "" && dni != "") {
            if (contrasena == contrasena2) {
                url = '/usuarios/createuser/'
                var formData = {
                    'username': usuario, 'password': contrasena, 'nombre': nombre, 'email': correo, 'apellidos': apellidos, "dni": dni, 'curso': tutor
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
                        if (respuesta == 'Duplicate username') {
                            alert("El nombre de usuario ya existe")
                        } else if (respuesta == 'dni repetido') {
                            alert("Ya existe un usuario con ese DNI")
                        } else {
                            window.location = '/usuarios/usuarios'
                        }
                    },
                    error: function() {
                        alert("Error al introducir el usuario");
                    }
                });
            } else {
                alert("Las contraseñas no coinciden")
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