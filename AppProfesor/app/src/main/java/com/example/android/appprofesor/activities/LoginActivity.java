package com.example.android.appprofesor.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.appprofesor.Connectors.LoginConnector;
import com.example.android.appprofesor.R;
import com.example.android.appprofesor.data.SettingsDAO;
import com.example.android.appprofesor.models.Settings;
import com.example.android.appprofesor.viewmodels.SettingsViewModel;

import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText passText;
    private Button loginButton;
    private boolean logged;

    private AlertDialog.Builder builder;
    private AlertDialog dialogAlumno;

    private SettingsViewModel model;
    private List<Settings> settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        model = ViewModelProviders.of(this).get(SettingsViewModel.class);
        model.getSettings().observe(this, new Observer<List<Settings>>() {
            @Override
            public void onChanged(List<Settings> settingsList) {
                settings = settingsList;
                if (!settings.isEmpty()) {
                    SharedPreferences prefs =
                            getSharedPreferences("serverSettings", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("address", settings.get(0).getAddress());
                    editor.putInt("port", settings.get(0).getPort());
                    editor.apply();
                }

            }
        });

        emailText = findViewById(R.id.input_email);
        passText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (settings.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    login();
                }
            }
        });
    }

    private void login() {
        String username = emailText.getText().toString();
        String password = passText.getText().toString();
        new ConectarServidor().execute(username, password);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            createPopupSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPopupSettings() {
        builder = new AlertDialog.Builder(LoginActivity.this);
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.popup_settings, null);

        builder.setView(view);

        dialogAlumno = builder.create();
        dialogAlumno.show();

        final EditText direccion = view.findViewById(R.id.popupAddress);
        final EditText puerto = view.findViewById(R.id.popupPort);

        if (!settings.isEmpty()) {
            direccion.setText(settings.get(0).getAddress());
            puerto.setText(settings.get(0).getPort() + "");
        }
        Button saveButton = view.findViewById(R.id.popupSaveSettings);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(direccion.getText()) && !TextUtils.isEmpty(puerto.getText())) {

                    if (settings.isEmpty()) {
                        try {
                            Settings set = new Settings(direccion.getText().toString(), Integer.parseInt(puerto.getText().toString()));
                            model.addSettings(set);
                            dialogAlumno.dismiss();
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplication(), "El puerto debe de ser un número", Toast.LENGTH_SHORT)
                                    .show();
                        } catch (Exception e) {
                            Toast.makeText(getApplication(), "Error modificando ajustes", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        try {
                            Settings set = settings.get(0);
                            set.setAddress(direccion.getText().toString());
                            set.setPort(Integer.parseInt(puerto.getText().toString()));
                            model.updateSettings(set);
                            dialogAlumno.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getApplication(), "Error modificando ajustes", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                }

            }
        });
    }

    private void comprobarSesion() {
        if (logged) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
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
            List<String> userData = login.iniciarSesion(strings[0], strings[1], settings.get(0));


            SharedPreferences prefs =
                    getSharedPreferences("userData", Context.MODE_PRIVATE);

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

}
