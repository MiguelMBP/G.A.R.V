package com.example.android.appprofesor.models;

import java.io.Serializable;
import java.util.List;

public class TutorAlumno implements Serializable {
    private String nombre;
    private List<TutorAsignatura> asignaturas;
    private String curso;

    public TutorAlumno(String nombre, List<TutorAsignatura> asignaturas, String curso) {
        this.nombre = nombre;
        this.asignaturas = asignaturas;
        this.curso = curso;
    }

    public TutorAlumno() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<TutorAsignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<TutorAsignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
