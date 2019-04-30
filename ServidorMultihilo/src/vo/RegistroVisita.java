package vo;

import java.util.Date;

public class RegistroVisita {

	private String docente;
	private Alumno alumno;
	private Date fecha;
	private boolean validada;
	private byte[] imagen;
	private String imagen64;

	public RegistroVisita(String docente, Alumno alumno, Date fecha, boolean validada, byte[] imagen, String imagen64) {
		super();
		this.docente = docente;
		this.alumno = alumno;
		this.fecha = fecha;
		this.validada = validada;
		this.imagen = imagen;
		this.imagen64 = imagen64;
	}

	public RegistroVisita() {
		}

	public String getDocente() {
		return docente;
	}

	public void setDocente(String docente) {
		this.docente = docente;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean isValidada() {
		return validada;
	}

	public void setValidada(boolean validada) {
		this.validada = validada;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getImagen64() {
		return imagen64;
	}

	public void setImagen64(String imagen64) {
		this.imagen64 = imagen64;
	}

	
}
