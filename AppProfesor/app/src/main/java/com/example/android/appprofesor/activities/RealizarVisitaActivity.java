package com.example.android.appprofesor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.models.Empresa;
import com.example.android.appprofesor.viewmodels.EmpresaViewModel;
import com.example.android.appprofesor.viewmodels.VisitaTodosViewModel;

import java.util.List;

public class RealizarVisitaActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private AlertDialog.Builder builder;
    private AlertDialog dialogAlumno;
    private AlertDialog dialogEmpresa;

    VisitaTodosViewModel model;

    List<Alumno> alumnos;
    Alumno alumnoSeleccionado;
    EmpresaViewModel empresaModel;
    List<Empresa> empresas;
    Empresa empresasSeleccionada;

    Button confirmarVisita;
    Button verEnMapa;
    Button añadirAlumno;
    Spinner spinnerAlumnos;
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
        añadirAlumno = findViewById(R.id.button_add_student);
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
                if (alumnoSeleccionado != null) {
                    Intent intent = new Intent(RealizarVisitaActivity.this, MapsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("empresa", alumnoSeleccionado.getEmpresa());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        añadirAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopUpAlumno();
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

    private void createPopUpAlumno() {
        builder = new AlertDialog.Builder(RealizarVisitaActivity.this);
        View view = LayoutInflater.from(RealizarVisitaActivity.this).inflate(R.layout.popup_student, null);

        builder.setView(view);

        dialogAlumno = builder.create();
        dialogAlumno.show();

        final EditText nombre = view.findViewById(R.id.popupStudentName);
        final EditText apellidos = view.findViewById(R.id.popupStudentLastName);
        final EditText dni = view.findViewById(R.id.popupStudentID);
        final EditText curso = view.findViewById(R.id.popupStudentGrade);
        final Spinner empresa = view.findViewById(R.id.spinnerCompany);
        Button saveButton = view.findViewById(R.id.popupSavePlayerButton);
        Button añadirEmpresa = view.findViewById(R.id.popupCreateCompanyButton);

        empresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                empresasSeleccionada = empresas.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(nombre.getText()) && !TextUtils.isEmpty(apellidos.getText()) && !TextUtils.isEmpty(dni.getText()) && !TextUtils.isEmpty(curso.getText())) {

                    añadirAlumno(nombre.getText().toString(), apellidos.getText().toString(), dni.getText().toString(), curso.getText().toString());

                }

            }
        });

        empresaModel = ViewModelProviders.of(this).get(EmpresaViewModel.class);
        empresaModel.getEmpresas().observe(this, new Observer<List<Empresa>>() {
            @Override
            public void onChanged(List<Empresa> empresasList) {
                String[] nombres = new String[empresasList.size()];
                for (int i = 0; i < empresasList.size(); i++) {
                    nombres[i] = empresasList.get(i).getNombre();
                }

                ArrayAdapter aa = new ArrayAdapter(RealizarVisitaActivity.this, R.layout.support_simple_spinner_dropdown_item, nombres);
                aa.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                empresa.setAdapter(aa);
                empresas = empresasList;
            }
        });

        añadirEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RealizarVisitaActivity.this, SelectMapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void añadirAlumno(String nombre, String apellidos, String dni, String curso) {
        Alumno alumno = new Alumno();
        alumno.setNombre(nombre);
        alumno.setApellidos(apellidos);
        alumno.setCurso(curso);
        alumno.setDni(dni);
        alumno.setEmpresa(empresasSeleccionada);

        model.addAlumno(alumno);

        dialogAlumno.dismiss();
    }

    private void actualizarInterfaz() {
        empresaTextView.setText(alumnoSeleccionado.getEmpresa().getNombre());
        localizacionTextView.setText(alumnoSeleccionado.getEmpresa().getDireccion() + ", " + alumnoSeleccionado.getEmpresa().getPoblacion());
        coordenadasTextView.setText(alumnoSeleccionado.getEmpresa().getLatitud() + ", " + alumnoSeleccionado.getEmpresa().getLongitud());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }


}
