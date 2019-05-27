package com.example.android.appprofesor.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import com.example.android.appprofesor.Connectors.WarningConnector;
import com.example.android.appprofesor.models.ClaseApercibimiento;
import com.example.android.appprofesor.models.Settings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Consulta y almacena la información referente a los objetos ClaseApercibimiento
 */
public class ClaseViewModel extends AndroidViewModel {
    private static MutableLiveData<List<ClaseApercibimiento>> clases;
    private Application application;

    public ClaseViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    /**
     * Ejecuta la clase asincrona ConectarServidor para consultar al servidor
     * @param settings
     * @return
     */
    public LiveData<List<ClaseApercibimiento>> getClases(Settings settings) {

        if (clases==null){
            clases= new MutableLiveData<>();
        }
        new ConectarServidor(settings).execute();
        return clases;
    }

    /**
     * Consulta los datos al servidor
     */
    private class ConectarServidor extends AsyncTask<Void, Void, List<ClaseApercibimiento>> {

        Settings settings;
        public ConectarServidor(Settings settings) {
            this.settings = settings;
        }

        @Override
        protected List<ClaseApercibimiento> doInBackground(Void... voids) {
            return WarningConnector.getMaterias(settings);
        }

        @Override
        protected void onPostExecute(List<ClaseApercibimiento> claseApercibimientos) {
            clases.setValue(claseApercibimientos);
        }
    }

}
