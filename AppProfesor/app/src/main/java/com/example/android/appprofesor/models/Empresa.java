package com.example.android.appprofesor.models;

import java.io.Serializable;

public class Empresa implements Serializable {
    private int id;
    private String cif;
    private String nombre;
    private String direccion;
    private String población;
    private float latitud;
    private float longitud;

    public Empresa(int id, String cif, String nombre, String direccion, String población, float coordinateX, float coordinateY) {
        this.id = id;
        this.cif = cif;
        this.nombre = nombre;
        this.direccion = direccion;
        this.población = población;
        this.latitud = coordinateX;
        this.longitud = coordinateY;
    }

    public Empresa() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
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

    public String getPoblación() {
        return población;
    }

    public void setPoblación(String población) {
        this.población = población;
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
}
