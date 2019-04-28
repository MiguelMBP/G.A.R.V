package com.example.android.appprofesor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.utils.Utils;
import com.example.android.appprofesor.viewmodels.VisitaTodosViewModel;
import com.example.android.appprofesor.viewmodels.VisitaViewModel;

import java.util.List;

public class RealizarVisitaActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    VisitaTodosViewModel model;

    Button confirmarVisita;
    Button verEnMapa;
    Spinner spinnerAlumnos;
    List<Alumno> alumnos;
    Alumno alumnoSeleccionado;
    TextView empresaTextView;
    TextView localizacionTextView;
    TextView coordenadasTextView;
    TextView distanciaTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_visita);
        confirmarVisita = findViewById(R.id.button_visit);
        verEnMapa = findViewById(R.id.button_map);
        spinnerAlumnos = findViewById(R.id.spinnerStudentVisitName);
        empresaTextView = findViewById(R.id.textViewCompanyDetailName);
        localizacionTextView = findViewById(R.id.textViewCompanyLoc);
        coordenadasTextView = findViewById(R.id.textViewCompanyCoordinate);
        distanciaTextView = findViewById(R.id.textViewCompanyDistance);
        this.imageView = findViewById(R.id.textViewStudentVisitIcon);

        spinnerAlumnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alumnoSeleccionado = alumnos.get(position);

                actualizarInterfaz();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        confirmarVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RealizarVisitaActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        verEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RealizarVisitaActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                //TODO
                bundle.putSerializable("empresa", alumnoSeleccionado.getEmpresa());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        model = ViewModelProviders.of(this).get(VisitaTodosViewModel.class);
        model.getTodosAlumnos().observe(this, new Observer<List<Alumno>>() {


            @Override
            public void onChanged(List<Alumno> alumnosList) {
                String[] nombres = new String[alumnosList.size()];
                for (int i = 0; i < alumnosList.size(); i++) {
                    nombres[i] = alumnosList.get(i).getNombre() + " " + alumnosList.get(i).getApellidos();
                }

                ArrayAdapter aa = new ArrayAdapter(RealizarVisitaActivity.this, R.layout.support_simple_spinner_dropdown_item, nombres);
                aa.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinnerAlumnos.setAdapter(aa);
                alumnos = alumnosList;
            }
        });


    }

    private void actualizarInterfaz() {
        empresaTextView.setText(alumnoSeleccionado.getEmpresa().getNombre());
        localizacionTextView.setText(alumnoSeleccionado.getEmpresa().getDireccion() + ", " + alumnoSeleccionado.getEmpresa().getPoblacion());
        coordenadasTextView.setText(alumnoSeleccionado.getEmpresa().getLatitud() + ", " + alumnoSeleccionado.getEmpresa().getLatitud());
        distanciaTextView.setText(alumnoSeleccionado.getEmpresa().getDistancia() + "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }


}
