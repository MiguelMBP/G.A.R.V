package vo;

/**
 * Objeto que representa un usuario
 * @author mmbernal
 *
 */
public class Usuario {

    private String usuario;
    private String nombre;
    private String apellidos;
    private String correo;
    private String cursoTutor;
    private String dni;

    public Usuario(String usuario, String nombre, String apellidos, String correo, String cursoTutor, String dni) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.cursoTutor = cursoTutor;
        this.dni = dni;
    }

    public Usuario() {
        super();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCursoTutor() {
        return cursoTutor;
    }

    public void setCursoTutor(String cursoTutor) {
        this.cursoTutor = cursoTutor;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
    
    

}
