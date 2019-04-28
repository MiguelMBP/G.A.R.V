package com.example.android.appprofesor.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.appprofesor.Connectors.VisitConnector;
import com.example.android.appprofesor.models.Alumno;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class VisitaViewModel extends AndroidViewModel {
    private static MutableLiveData<List<Alumno>> alumnos;
    private Application application;

    public VisitaViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<List<Alumno>> getAlumnos(Context context) {

        if (alumnos==null){
            alumnos= new MutableLiveData<>();
            new ConectarServidorAlumnos().execute(context);
        }
        return alumnos;
    }

    public LiveData<List<Alumno>> getTodosAlumnos() {

        if (alumnos==null){
            alumnos= new MutableLiveData<>();
            new ConectarServidorTodosAlumnos().execute();
        }
        return alumnos;
    }

    private class ConectarServidorAlumnos extends AsyncTask<Context, Void, List<Alumno>> {

        @Override
        protected List<Alumno> doInBackground(Context... contexts) {
            return VisitConnector.getAlumnos(contexts[0]);
        }

        @Override
        protected void onPostExecute(List<Alumno> alumnosVisita) {
            alumnos.setValue(alumnosVisita);
        }
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
}
