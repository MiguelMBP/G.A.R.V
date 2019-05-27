package com.example.android.appprofesor.models;

import java.util.Date;

/**
 * Objeto que representa una visita para introducir en la base de datos
 * @author mmbernal
 *
 */
public class RegistroVisita {
	private String docente;
	private Alumno alumno;
	private Date fecha;
	private boolean validada;
	private String imagen64;
	private String csrfToken;
	private String sessionId;

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

	public String getImagen64() {
		return imagen64;
	}

	public void setImagen64(String imagen64) {
		this.imagen64 = imagen64;
	}

	public String getCsrfToken() {
		return csrfToken;
	}

	public void setCsrfToken(String csrfToken) {
		this.csrfToken = csrfToken;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
