package com.example.android.appprofesor.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.android.appprofesor.Connectors.VisitConnector;
import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.models.Empresa;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class VisitaTodosViewModel extends AndroidViewModel {
    private static MutableLiveData<List<Alumno>> alumnos;
    private Application application;

    public VisitaTodosViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<List<Alumno>> getTodosAlumnos() {

        if (alumnos==null){
            alumnos= new MutableLiveData<>();
            new ConectarServidorTodosAlumnos().execute();
        }
        return alumnos;
    }

    public void addAlumno(Alumno alumno) {

        new ConectarServidorAñadirAlumno().execute(alumno);
    }

    private class ConectarServidorTodosAlumnos extends AsyncTask<Void, Void, List<Alumno>> {

        @Override
        protected List<Alumno> doInBackground(Void... voids) {
            return VisitConnector.getTodosAlumnos();
        }

        @Override
        protected void onPostExecute(List<Alumno> alumnosVisita) {
            alumnos.setValue(alumnosVisita);
        }
    }

    private class ConectarServidorAñadirAlumno extends AsyncTask<Alumno, Void, Integer> {

        Alumno alumno;

        @Override
        protected Integer doInBackground(Alumno... alumnos) {
            int id = -1;

            if (alumnos.length != 0) {
                alumno = alumnos[0];
                id = VisitConnector.addAlumno(alumno);

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
}
