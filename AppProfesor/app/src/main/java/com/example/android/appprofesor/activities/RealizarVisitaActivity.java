package com.example.android.appprofesor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.models.Empresa;
import com.example.android.appprofesor.models.RegistroVisita;
import com.example.android.appprofesor.viewmodels.EmpresaViewModel;
import com.example.android.appprofesor.viewmodels.VisitaTodosViewModel;
import com.google.android.material.picker.MaterialDatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RealizarVisitaActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private AlertDialog.Builder builder;
    private AlertDialog dialogAlumno;

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
    EditText fechaVisita;
    int mYear;
    int mMonth;
    int mDay;


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
        fechaVisita = findViewById(R.id.visitDate);

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
                    registrarVisita();
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

        fechaVisita.setFocusable(false);
        fechaVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(RealizarVisitaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        fechaVisita.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


    }

    private void registrarVisita() {
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
        try {
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                SharedPreferences prefs =
                        this.getSharedPreferences("userData", Context.MODE_PRIVATE);

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");

                Date fecha = sdf.parse(fechaVisita.getText().toString());


                RegistroVisita visita = new RegistroVisita();
                visita.setDocente(prefs.getString("username", "error"));
                visita.setAlumno(alumnoSeleccionado);
                visita.setImagen(byteArray);
                visita.setImagen64(base64);
                visita.setFecha(fecha);

                model.addVisita(visita);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
