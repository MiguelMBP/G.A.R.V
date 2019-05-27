package com.example.android.appprofesor.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.appprofesor.Connectors.VisitConnector;
import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.models.Settings;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Consulta y almacena la información referente a los objetos Alumno
 */
public class VisitaViewModel extends AndroidViewModel {
    private static MutableLiveData<List<Alumno>> alumnos;
    private Application application;

    public VisitaViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    /**
     * Ejecuta la clase asincrona ConectarServidorAlumnos para consultar al servidor
     * @param context
     * @param settings
     * @return
     */
    public LiveData<List<Alumno>> getAlumnos(Context context, Settings settings) {
        if (alumnos == null) {
            alumnos = new MutableLiveData<>();
        }
        new ConectarServidorAlumnos(settings).execute(context);

        return alumnos;
    }

    /**
     * Consulta los datos al servidor
     */
    private class ConectarServidorAlumnos extends AsyncTask<Context, Void, List<Alumno>> {
        Settings settings;

        public ConectarServidorAlumnos(Settings settings) {
            this.settings = settings;
        }

        @Override
        protected List<Alumno> doInBackground(Context... contexts) {
            return VisitConnector.getAlumnos(contexts[0], settings);
        }

        @Override
        protected void onPostExecute(List<Alumno> alumnosVisita) {
            alumnos.setValue(alumnosVisita);
        }
    }

}
