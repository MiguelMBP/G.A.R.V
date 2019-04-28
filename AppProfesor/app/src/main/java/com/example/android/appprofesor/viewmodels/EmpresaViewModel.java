package com.example.android.appprofesor.viewmodels;

import android.app.Application;
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

public class EmpresaViewModel extends AndroidViewModel {
    private static MutableLiveData<List<Empresa>> empresas;
    private Application application;

    public EmpresaViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<List<Empresa>> getEmpresas() {

        if (empresas==null){
            empresas= new MutableLiveData<>();
            new ConectarServidorEmpresas().execute();
        }
        return empresas;
    }

    public void addEmpresa(Empresa empresa) {

        new ConectarServidorAñadirEmpresa().execute(empresa);
    }

    private class ConectarServidorEmpresas extends AsyncTask<Void, Void, List<Empresa>> {

        @Override
        protected List<Empresa> doInBackground(Void... voids) {
            return VisitConnector.getEmpresas();
        }

        @Override
        protected void onPostExecute(List<Empresa> empresasList) {
            empresas.setValue(empresasList);
        }
    }

    private class ConectarServidorAñadirEmpresa extends AsyncTask<Empresa, Void, Integer> {

        Empresa empresa;

        @Override
        protected Integer doInBackground(Empresa... empresas) {
            int id = -1;

            if (empresas.length != 0) {
                empresa = empresas[0];
                id = VisitConnector.addEmpresa(empresa);

                empresa.setId(id);
            }

            return id;
        }

        @Override
        protected void onPostExecute(Integer updatedRows) {
            if (updatedRows == -1) {
                Toast.makeText(getApplication(), "Error añadiendo Empresa", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplication(), "Empresa añadida", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
