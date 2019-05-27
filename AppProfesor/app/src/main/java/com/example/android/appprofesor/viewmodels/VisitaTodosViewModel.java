package com.example.android.appprofesor.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.android.appprofesor.Connectors.VisitConnector;
import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.models.Empresa;
import com.example.android.appprofesor.models.RegistroVisita;
import com.example.android.appprofesor.models.Settings;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Consulta y almacena la información referente a los objetos Alumno
 */
public class VisitaTodosViewModel extends AndroidViewModel {
    private static MutableLiveData<List<Alumno>> alumnos;
    private Application application;

    public VisitaTodosViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    /**
     * Ejecuta la clase asincrona ConectarServidorTodosAlumnos para consultar al servidor
     * @param settings
     * @return
     */
    public LiveData<List<Alumno>> getTodosAlumnos(Settings settings) {
        if (alumnos == null) {
            alumnos = new MutableLiveData<>();
        }
        new ConectarServidorTodosAlumnos(settings).execute();
        return alumnos;
    }

    /**
     * Ejecuta la clase asincrona ConectarServidorAñadirAlumno para añadir un alumno
     * @param alumno
     * @param settings
     */
    public void addAlumno(Alumno alumno, Settings settings) {

        new ConectarServidorAñadirAlumno(settings).execute(alumno);
        //new ConectarServidorTodosAlumnos().execute();
    }

    /**
     * Ejecuta la clase asincrona ConectarServidorAñadirAlumno para registrar una visita
     * @param visita
     * @param settings
     */
    public void addVisita(RegistroVisita visita, Settings settings) {

        new ConectarServidorRegistrarVisita(settings).execute(visita);
    }

    /**
     * Consulta los datos al servidor
     */
    private class ConectarServidorTodosAlumnos extends AsyncTask<Void, Void, List<Alumno>> {

        Alumno alumno;
        Settings settings;

        public ConectarServidorTodosAlumnos(Settings settings) {
            this.settings = settings;
        }


        @Override
        protected List<Alumno> doInBackground(Void... voids) {
            return VisitConnector.getTodosAlumnos(settings);
        }

        @Override
        protected void onPostExecute(List<Alumno> alumnosVisita) {
            alumnos.setValue(alumnosVisita);
        }
    }

    /**
     * Inserta un alumno
     */
    private class ConectarServidorAñadirAlumno extends AsyncTask<Alumno, Void, Integer> {

        Alumno alumno;
        Settings settings;

        public ConectarServidorAñadirAlumno(Settings settings) {
            this.settings = settings;
        }

        @Override
        protected Integer doInBackground(Alumno... alumnos) {
            int id = -1;

            if (alumnos.length != 0) {
                alumno = alumnos[0];
                id = VisitConnector.addAlumno(alumno, settings);

                alumno.setId(id);
            }

            return id;
        }

        @Override
        protected void onPostExecute(Integer updatedRows) {
            if (updatedRows == -1) {
                Toast.makeText(getApplication(), "Error añadiendo Alumno", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplication(), "Alumno añadido", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Registra una visita
     */
    private class ConectarServidorRegistrarVisita extends AsyncTask<RegistroVisita, Void, Integer> {

        RegistroVisita visita;
        Settings settings;

        public ConectarServidorRegistrarVisita(Settings settings) {
            this.settings = settings;
        }

        @Override
        protected Integer doInBackground(RegistroVisita... visitas) {
            int id = -1;

            if (visitas.length != 0) {
                visita = visitas[0];
                id = VisitConnector.addVisita(visita, settings);
            }

            return id;
        }

        @Override
        protected void onPostExecute(Integer updatedRows) {
            if (updatedRows == -1) {
                Toast.makeText(getApplication(), "Error añadiendo Alumno", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplication(), "Alumno añadido", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
