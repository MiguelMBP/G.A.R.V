package vo;

import java.io.Serializable;
import java.util.Date;

public class Alumno implements Serializable {

    private String nombre;
    private String apellidos;
    private Empresa empresa;
    private Date fecha;

    

    public Alumno(String nombre, String apellidos, Empresa empresa, Date fecha) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.empresa = empresa;
		this.fecha = fecha;
	}

	public Alumno() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
    
    
}