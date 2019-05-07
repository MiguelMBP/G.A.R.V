package com.example.android.appprofesor.viewmodels;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.android.appprofesor.data.DataBaseRoom;
import com.example.android.appprofesor.models.Settings;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SettingsViewModel extends AndroidViewModel {
    private static DataBaseRoom db;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        db = DataBaseRoom.getInstance(application);
    }

    public LiveData<List<Settings>> getSettings() {
        return db.settingsDAO().getAll();
    }

    public void addSettings(Settings settings) {
        new AsyncAddSettings().execute(settings);
    }

    public void updateSettings(Settings settings){
        new AsyncEditSettingsDB().execute(settings);
    }

    private class AsyncAddSettings extends AsyncTask<Settings, Void, Long> {

        @Override
        protected Long doInBackground(Settings... settings) {

            long id = -1;

            if (settings.length != 0) {
                id = db.settingsDAO().insertSettings(settings[0]);
            }

            return id;
        }

        @Override
        protected void onPostExecute(Long id) {
            if (id == -1) {
                Toast.makeText(getApplication(), "Error modificando ajustes", Toast.LENGTH_SHORT)
                        .show();
            } else {

                Toast.makeText(getApplication(), "Ajustes modificados", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private class AsyncEditSettingsDB extends AsyncTask<Settings, Void, Integer> {

        @Override
        protected Integer doInBackground(Settings... settings) {
            int updatedrows = 0;
            if (settings.length != 0) {
                updatedrows = db.settingsDAO().updateSettings(settings[0]);
            }

            return updatedrows;
        }

        @Override
        protected void onPostExecute(Integer updatedRows) {
            if (updatedRows == 0) {
                Toast.makeText(getApplication(), "Error modificando ajustes", Toast.LENGTH_SHORT)
                        .show();
            } else {

                Toast.makeText(getApplication(), "Ajustes modificados", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
