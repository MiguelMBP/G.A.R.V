package com.example.android.appprofesor.models;

import java.io.Serializable;
import java.util.List;

public class ClaseApercibimiento implements Serializable {

    private int id;
    private List<AlumnoApercibimiento> alumnos;
    private String curso;
    private String asignatura;

    public ClaseApercibimiento(int id, List<AlumnoApercibimiento> alumnos, String curso, String asignatura) {
        this.id = id;
        this.alumnos = alumnos;
        this.curso = curso;
        this.asignatura = asignatura;
    }

    public ClaseApercibimiento() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AlumnoApercibimiento> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<AlumnoApercibimiento> alumnos) {
        this.alumnos = alumnos;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }
}
