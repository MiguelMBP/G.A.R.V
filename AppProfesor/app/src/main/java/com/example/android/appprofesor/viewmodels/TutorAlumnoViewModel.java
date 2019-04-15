package com.example.android.appprofesor.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import com.example.android.appprofesor.Connectors.WarningConnector;
import com.example.android.appprofesor.models.TutorAlumno;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TutorAlumnoViewModel extends AndroidViewModel {

    private static MutableLiveData<List<TutorAlumno>> alumnos;
    private Application application;

    public TutorAlumnoViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<List<TutorAlumno>> getAlumnos() {

        if (alumnos==null){
            alumnos= new MutableLiveData<>();
            new ConectarServidor().execute();
        }
        return alumnos;
    }

    private void cargarClases() {
        new ConectarServidor().execute();
    }

    private class ConectarServidor extends AsyncTask<Void, Void, List<TutorAlumno>> {

        @Override
        protected List<TutorAlumno> doInBackground(Void... voids) {
            return WarningConnector.getAlumnosTutor();
        }

        @Override
        protected void onPostExecute(List<TutorAlumno> tutorAlumnos) {
            alumnos.setValue(tutorAlumnos);
        }
    }
}
