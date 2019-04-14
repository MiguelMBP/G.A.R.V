package com.example.android.appprofesor.utils;

import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.models.AlumnoApercibimiento;
import com.example.android.appprofesor.models.ClaseApercibimiento;
import com.example.android.appprofesor.models.Empresa;
import com.example.android.appprofesor.models.TutorAlumno;
import com.example.android.appprofesor.models.TutorAsignatura;

import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static List<ClaseApercibimiento> getDummyClass() {
        ArrayList<ClaseApercibimiento> clases = new ArrayList<>();
        ArrayList<AlumnoApercibimiento> alumnos = new ArrayList<>();

        alumnos.add(new AlumnoApercibimiento("Manuel", new ArrayList<String>(){{add("enero"); add("febrero");}}));
        alumnos.add(new AlumnoApercibimiento("Paco", new ArrayList<String>(){{add("Enero"); add("Febrero"); add("Marzo");}}));

        clases.add(new ClaseApercibimiento("PSP", "2º DAM", alumnos));
        clases.add(new ClaseApercibimiento("SSII", "1º DAM", alumnos));

        return clases;
    }

    public static List<TutorAlumno> getDummyStudent() {
        ArrayList<TutorAlumno> alumnos = new ArrayList<>();
        ArrayList<TutorAsignatura> asignaturas = new ArrayList<>();
        asignaturas.add(new TutorAsignatura("SSII", new ArrayList<String>(){{add("Enero"); add("Febrero");}}));
        asignaturas.add(new TutorAsignatura("BBDD", new ArrayList<String>(){{add("Diciembre"); add("Febrero");}}));
        alumnos.add(new TutorAlumno("Pepe", asignaturas, "1º DAM"));

        asignaturas = new ArrayList<>();
        asignaturas.add(new TutorAsignatura("ED", new ArrayList<String>(){{add("Enero"); add("Febrero");}}));
        asignaturas.add(new TutorAsignatura("Pr", new ArrayList<String>(){{add("Diciembre"); add("Febrero"); add("Marzo");}}));
        alumnos.add(new TutorAlumno("Paco", asignaturas, "1º DAM"));

        return alumnos;

    }

    public static List<Alumno> getDummyVisit() {
        ArrayList<Alumno> alumnos = new ArrayList<>();
        alumnos.add(new Alumno(1, "123", "Antonio", "Garcia", new Empresa(1, "465", "ATComponentes", "Calle Desarrollo, 10", "Jerez", 108.012f, 50.123f)));
        alumnos.add(new Alumno(2, "321", "Juan", "Sanchez", new Empresa(2, "654", "Empresa 2", "Calle Delirio, 21", "Cadiz", 108.012f, 50.123f)));
        return alumnos;
    }
}
