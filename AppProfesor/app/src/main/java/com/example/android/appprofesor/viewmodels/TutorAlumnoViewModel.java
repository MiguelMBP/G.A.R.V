package com.example.android.appprofesor.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.appprofesor.Connectors.WarningConnector;
import com.example.android.appprofesor.models.Empresa;
import com.example.android.appprofesor.models.Settings;
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

    public LiveData<List<TutorAlumno>> getAlumnos(Context context, Settings settings) {
        if (alumnos == null) {
            alumnos = new MutableLiveData<>();
        }
        new ConectarServidor(settings).execute(context);

        return alumnos;
    }

    private class ConectarServidor extends AsyncTask<Context, Void, List<TutorAlumno>> {

        Empresa empresa;
        Settings settings;

        public ConectarServidor(Settings settings) {
            this.settings = settings;
        }

        @Override
        protected List<TutorAlumno> doInBackground(Context... contexts) {
            return WarningConnector.getAlumnosTutor(contexts[0], settings);
        }

        @Override
        protected void onPostExecute(List<TutorAlumno> tutorAlumnos) {
            alumnos.setValue(tutorAlumnos);
        }
    }
}
