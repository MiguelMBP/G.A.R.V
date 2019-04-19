package com.example.android.appprofesor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.appprofesor.Connectors.LoginConnector;
import com.example.android.appprofesor.R;


public class LoginActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText passText;
    private Button loginButton;
    private boolean logged;

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
            return login.iniciarSesion(strings[0], strings[1]);
        }
    }

}
