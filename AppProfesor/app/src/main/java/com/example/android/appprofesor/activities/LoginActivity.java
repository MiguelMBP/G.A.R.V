package com.example.android.appprofesor.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText passText;
    private Button loginButton;
    private boolean logged;

    private AlertDialog.Builder builder;
    private AlertDialog dialogAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailText = findViewById(R.id.input_email);
        passText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
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
        Button saveButton = view.findViewById(R.id.popupSaveSettings);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(direccion.getText()) && !TextUtils.isEmpty(puerto.getText())) {

                    //TODO room

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
            List<String> userData = login.iniciarSesion(strings[0], strings[1]);

            SharedPreferences prefs =
                    getSharedPreferences("userData", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("csrftoken", userData.get(0));
            editor.putString("sessionid", userData.get(1));
            editor.putString("username", userData.get(2));
            editor.apply();

            return !userData.isEmpty();
        }
    }

}
