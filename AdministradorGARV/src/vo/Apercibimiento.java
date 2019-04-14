package vo;

import java.util.Date;



public class Apercibimiento {
	private int id;
	private String alumno;
	private int periodoAcademico;
	private String curso;
	private String unidad;
	private String materia;
	private Date fechaInicio;
	private Date fechaFin;
	private String horasJustificadas;
	private float porcentajeJustificado;
	private String horasInjustificadas;
	private float porcentajeInjustificado;
	private String retrasos;
	private boolean activo;
	
	
	
	public Apercibimiento(int id, String alumno, int periodoAcademico, String curso, String unidad, String materia,
			Date fechaInicio, Date fechaFin, String horasJustificadas, float porcentajeJustificado,
			String horasInjustificadas, float porcentajeInjustificado, String retrasos, boolean activo) {
		super();
		this.id = id;
		this.alumno = alumno;
		this.periodoAcademico = periodoAcademico;
		this.curso = curso;
		this.unidad = unidad;
		this.materia = materia;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.horasJustificadas = horasJustificadas;
		this.porcentajeJustificado = porcentajeJustificado;
		this.horasInjustificadas = horasInjustificadas;
		this.porcentajeInjustificado = porcentajeInjustificado;
		this.retrasos = retrasos;
		this.activo = activo;
	}

	public Apercibimiento() {
		super();
	}

	public String getAlumno() {
		return alumno;
	}

	public void setAlumno(String alumno) {
		this.alumno = alumno;
	}

	public int getPeriodoAcademico() {
		return periodoAcademico;
	}

	public void setPeriodoAcademico(int periodoAcademico) {
		this.periodoAcademico = periodoAcademico;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getHorasJustificadas() {
		return horasJustificadas;
	}

	public void setHorasJustificadas(String horasJustificadas) {
		this.horasJustificadas = horasJustificadas;
	}

	public float getPorcentajeJustificado() {
		return porcentajeJustificado;
	}

	public void setPorcentajeJustificado(float porcentajeJustificado) {
		this.porcentajeJustificado = porcentajeJustificado;
	}

	public String getHorasInjustificadas() {
		return horasInjustificadas;
	}

	public void setHorasInjustificadas(String horasInjustificadas) {
		this.horasInjustificadas = horasInjustificadas;
	}

	public float getPorcentajeInjustificado() {
		return porcentajeInjustificado;
	}

	public void setPorcentajeInjustificado(float porcentajeInjustificado) {
		this.porcentajeInjustificado = porcentajeInjustificado;
	}

	public String getRetrasos() {
		return retrasos;
	}

	public void setRetrasos(String retrasos) {
		this.retrasos = retrasos;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Apercibimiento [id=" + id + ", alumno=" + alumno + ", periodoAcademico=" + periodoAcademico + ", curso="
				+ curso + ", unidad=" + unidad + ", materia=" + materia + ", fechaInicio=" + fechaInicio + ", fechaFin="
				+ fechaFin + ", horasJustificadas=" + horasJustificadas + ", porcentajeJustificado="
				+ porcentajeJustificado + ", horasInjustificadas=" + horasInjustificadas + ", porcentajeInjustificado="
				+ porcentajeInjustificado + ", retrasos=" + retrasos + ", activo=" + activo + "]";
	}

	
	
	
}
