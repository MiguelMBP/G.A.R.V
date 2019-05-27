package com.example.android.appprofesor.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto que representa una asignatura con sus alumnos
 * @author mmbernal
 *
 */
public class ClaseApercibimiento implements Serializable {

    private String materia;
    private String unidad;
    private List<AlumnoApercibimiento> alumnos;

    public ClaseApercibimiento(String materia, String unidad, List<AlumnoApercibimiento> alumnos) {
        super();
        this.materia = materia;
        this.unidad = unidad;
        this.alumnos = alumnos;
    }

    public ClaseApercibimiento() {
        super();
        alumnos = new ArrayList<>();
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }



    public List<AlumnoApercibimiento> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<AlumnoApercibimiento> alumnos) {
        this.alumnos = alumnos;
    }

    @Override
    public String toString() {
        return "Materia [materia=" + materia + ", unidad=" + unidad + ", alumnos=" + alumnos + "]";
    }
}
