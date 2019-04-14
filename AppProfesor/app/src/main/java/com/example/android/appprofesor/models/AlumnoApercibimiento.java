package com.example.android.appprofesor.models;

import java.io.Serializable;
import java.util.List;

public class AlumnoApercibimiento implements Serializable {
    private String nombre;
    private List<String> meses;

    public AlumnoApercibimiento(String nombre, List<String> meses) {
        this.nombre = nombre;
        this.meses = meses;
    }

    public AlumnoApercibimiento() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getMeses() {
        return meses;
    }

    public void setMeses(List<String> meses) {
        this.meses = meses;
    }

    public String getMesesString() {
        String mes = "";
        for (int i = 0; i < meses.size(); i++) {
            mes += meses.get(i);
            if (i < meses.size() - 1) {
                mes += ", ";
            }
        }
        return mes;
    }

    @Override
    public String toString() {
        return "AlumnoApercibimiento [nombre=" + nombre + ", meses=" + meses + "]";
    }

}
