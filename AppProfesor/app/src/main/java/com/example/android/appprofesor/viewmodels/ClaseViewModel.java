package com.example.android.appprofesor.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import com.example.android.appprofesor.Connectors.WarningConnector;
import com.example.android.appprofesor.models.ClaseApercibimiento;
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

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ClaseViewModel extends AndroidViewModel {
    private static MutableLiveData<List<ClaseApercibimiento>> clases;
    private Application application;

    public ClaseViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<List<ClaseApercibimiento>> getClases() {

        if (clases==null){
            clases= new MutableLiveData<>();
            new ConectarServidor().execute();
        }
        return clases;
    }

    private void cargarClases() {
        new ConectarServidor().execute();
    }

    private class ConectarServidor extends AsyncTask<Void, Void, List<ClaseApercibimiento>> {

        @Override
        protected List<ClaseApercibimiento> doInBackground(Void... voids) {
            return WarningConnector.getMaterias();
        }

        @Override
        protected void onPostExecute(List<ClaseApercibimiento> claseApercibimientos) {
            clases.setValue(claseApercibimientos);
        }
    }

}
