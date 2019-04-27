package vo;


import java.io.Serializable;

public class Empresa implements Serializable {
    private String nombre;
    private String direccion;
    private String poblacion;
    private float latitud;
    private float longitud;
    private float distancia;

    public Empresa(String nombre, String direccion, String poblacion, float latitud, float longitud, float distancia) {
		super();
		this.nombre = nombre;
		this.direccion = direccion;
		this.poblacion = poblacion;
		this.latitud = latitud;
		this.longitud = longitud;
		this.distancia = distancia;
	}

	public Empresa() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

	public float getDistancia() {
		return distancia;
	}

	public void setDistancia(float distancia) {
		this.distancia = distancia;
	}
    
    
}