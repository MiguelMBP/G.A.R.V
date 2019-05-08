package com.example.android.appprofesor.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.appprofesor.Connectors.LoginConnector;
import com.example.android.appprofesor.R;
import com.example.android.appprofesor.activities.MainActivity;
import com.example.android.appprofesor.models.Settings;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChangePassFragment extends Fragment {

    private EditText contraseñaActualText;
    private EditText contraseñaNuevaText;
    private EditText contraseñaNueva2Text;
    private Button confirmar;
    private Settings settings;
    private boolean logged;

    public ChangePassFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pass, container, false);

        contraseñaActualText = view.findViewById(R.id.actualPassText);
        contraseñaNuevaText = view.findViewById(R.id.newPassText1);
        contraseñaNueva2Text = view.findViewById(R.id.newPassText2);
        confirmar = view.findViewById(R.id.changePassButton);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs =
                        getContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
                String username = prefs.getString("username", null);
                LoginConnector lc = new LoginConnector();

                prefs = getContext().getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
                String address = prefs.getString("address", null);
                int port = prefs.getInt("port", -1);
                if (address != null && port != -1) {
                    settings = new Settings(address, port);
                    new ConectarServidor().execute(username, contraseñaActualText.getText().toString());
                }
            }
        });
        return view;
    }

    private void comprobarSesion() {
        SharedPreferences prefs =
                getContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String username = prefs.getString("username", null);
        String csrf = prefs.getString("csrftoken", "error");
        String sessionId = prefs.getString("sessionid", "error");

        prefs = getContext().getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
        String address = prefs.getString("address", null);
        int port = prefs.getInt("port", -1);

        if (address != null && port != -1) {
            settings = new Settings(address, port);
            if (logged) {
                if (contraseñaNuevaText.getText().toString().equals(contraseñaNueva2Text.getText().toString())) {



                    new ConectarServidorContraseña(username, csrf, sessionId).execute(contraseñaNuevaText.getText().toString());
                } else {
                    Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ConectarServidor extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean loggedb) {
            logged = loggedb;
            comprobarSesion();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            LoginConnector login = new LoginConnector();
            List<String> userData = login.iniciarSesion(strings[0], strings[1], settings);


            SharedPreferences prefs =
                    getContext().getSharedPreferences("userData", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();
            if (userData.size() != 3) {
                return false;
            }
            editor.putString("csrftoken", userData.get(0));
            editor.putString("sessionid", userData.get(1));
            editor.putString("username", userData.get(2));
            editor.apply();

            return !userData.isEmpty();

        }
    }

    private class ConectarServidorContraseña extends AsyncTask<String, Void, Boolean> {

        private String username;
        private String csrf;
        private String sessionId;

        public ConectarServidorContraseña(String username, String csrf, String sessionId) {
            this.username = username;
            this.csrf = csrf;
            this.sessionId = sessionId;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            LoginConnector login = new LoginConnector();
            boolean cambiado = login.cambiarContraseña(username, strings[0], csrf, sessionId, settings);
            return cambiado;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(getContext(), "Contraseña cambiada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
