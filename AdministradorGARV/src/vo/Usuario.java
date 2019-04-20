package vo;

public class Usuario {

	private String usuario;
	private String nombre;
	private String correo;
	private String cursoTutor;
	
	public Usuario(String usuario, String nombre, String correo, String cursoTutor) {
		super();
		this.usuario = usuario;
		this.nombre = nombre;
		this.correo = correo;
		this.cursoTutor = cursoTutor;
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
	
	
}
