package vo;

import java.util.Date;

/**
 * Objeto que representa una visita
 * @author mmbernal
 *
 */
public class Visita {
	private int id;
	private String docente;
	private String alumno;
	private String empresa;
	private String poblacion;
	private Date fecha;
	private double distancia;
	private boolean validada;
	
	

	public Visita(String docente, String alumno, String empresa, String poblacion, Date fecha, double distancia,
			boolean validada) {
		super();
		this.docente = docente;
		this.alumno = alumno;
		this.empresa = empresa;
		this.poblacion = poblacion;
		this.fecha = fecha;
		this.distancia = distancia;
		this.validada = validada;
	}

	public Visita() {
		super();
	}

	public String getDocente() {
		return docente;
	}

	public void setDocente(String docente) {
		this.docente = docente;
	}

	public String getAlumno() {
		return alumno;
	}

	public void setAlumno(String alumno) {
		this.alumno = alumno;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public boolean isValidada() {
		return validada;
	}

	public void setValidada(boolean validada) {
		this.validada = validada;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
