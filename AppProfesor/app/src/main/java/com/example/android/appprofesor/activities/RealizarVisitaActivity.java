package com.example.android.appprofesor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.example.android.appprofesor.models.Settings;
import com.example.android.appprofesor.viewmodels.EmpresaViewModel;
import com.example.android.appprofesor.viewmodels.SettingsViewModel;
import com.example.android.appprofesor.viewmodels.VisitaTodosViewModel;
import com.google.android.material.picker.MaterialDatePickerDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Activity donde se introducen los datos necesarios para realizar una visita, crear un alumno y/o una empresa
 */
public class RealizarVisitaActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private AlertDialog.Builder builder;
    private AlertDialog dialogAlumno;
    private AlertDialog dialogEmpresa;

    private VisitaTodosViewModel model;

    private List<Alumno> alumnos;
    private Alumno alumnoSeleccionado;
    private EmpresaViewModel empresaModel;
    private List<Empresa> empresas;
    private Empresa empresasSeleccionada;

    private Button confirmarVisita;
    private Button verEnMapa;
    private Button añadirAlumno;
    private Spinner spinnerAlumnos;
    private TextView empresaTextView;
    private TextView localizacionTextView;
    private TextView coordenadasTextView;
    private TextView distanciaTextView;
    private EditText fechaVisita;

    private int mYear;
    private int mMonth;
    private int mDay;
    private Uri image;
    private String currentPhotoPath;


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
                    if (!fechaVisita.getText().toString().equals("")) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                } else {
                    dispatchTakePictureIntent();
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
        cargarAlumnos();

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
                        String myFormat = "dd/MM/yyyy"; //Change as you need
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

    private void cargarAlumnos() {
        try {
            SharedPreferences prefs =
                    this.getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
            String address = prefs.getString("address", null);
            int port = prefs.getInt("port", -1);
            if (address != null && port != -1) {
                Settings settings = new Settings(address, port);
                model.getTodosAlumnos(settings).observe(this, new Observer<List<Alumno>>() {
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
            } else {
                throw new NullPointerException();
            }

        } catch (NullPointerException e) {
            Toast.makeText(this, "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                    .show();
        }
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
        Button empresaManual = view.findViewById(R.id.popupCreateCompanyManualButton);

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
                    cargarAlumnos();

                }

            }
        });

        empresaModel = ViewModelProviders.of(this).get(EmpresaViewModel.class);
        cargarEmpresas(empresa);

        añadirEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RealizarVisitaActivity.this, SelectMapActivity.class);
                startActivity(intent);
                cargarEmpresas(empresa);
            }
        });

        empresaManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopUpEmpresa();
                cargarEmpresas(empresa);
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

        SharedPreferences prefs =
                this.getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
        String address = prefs.getString("address", null);
        int port = prefs.getInt("port", -1);
        if (address != null && port != -1) {
            Settings settings = new Settings(address, port);
            model.addAlumno(alumno, settings);
        } else {
            Toast.makeText(this, "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                    .show();
        }

        dialogAlumno.dismiss();
    }

    private void actualizarInterfaz() {
        empresaTextView.setText(alumnoSeleccionado.getEmpresa().getNombre());
        localizacionTextView.setText(alumnoSeleccionado.getEmpresa().getDireccion() + ", " + alumnoSeleccionado.getEmpresa().getPoblacion());
        coordenadasTextView.setText(alumnoSeleccionado.getEmpresa().getLatitud() + ", " + alumnoSeleccionado.getEmpresa().getLongitud());
        distanciaTextView.setText(alumnoSeleccionado.getEmpresa().getDistancia() + "");
    }

    private void cargarEmpresas(final Spinner empresa) {
        try {
            SharedPreferences prefs =
                    this.getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
            String address = prefs.getString("address", null);
            int port = prefs.getInt("port", -1);
            if (address != null && port != -1) {
                Settings settings = new Settings(address, port);
                empresaModel.getEmpresas(settings).observe(this, new Observer<List<Empresa>>() {
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
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

                dispatchTakePictureIntent();

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

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);

                /*Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);*/

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                byte[] byteArray = stream.toByteArray();

                String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");

                Date fecha = sdf.parse(fechaVisita.getText().toString());

                RegistroVisita visita = new RegistroVisita();
                visita.setDocente(prefs.getString("username", "error"));
                visita.setAlumno(alumnoSeleccionado);
                visita.setImagen64(base64);
                visita.setFecha(fecha);
                visita.setCsrfToken(prefs.getString("csrftoken", "error"));
                visita.setSessionId(prefs.getString("sessionid", "error"));

                prefs = this.getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
                String address = prefs.getString("address", null);
                int port = prefs.getInt("port", -1);
                if (address != null && port != -1) {
                    Settings settings = new Settings(address, port);
                    model.addVisita(visita, settings);
                } else {
                    Toast.makeText(this, "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error occurred while creating the File", Toast.LENGTH_LONG).show();
                ex.printStackTrace();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private void createPopUpEmpresa() {
        builder = new AlertDialog.Builder(RealizarVisitaActivity.this);
        View view = LayoutInflater.from(RealizarVisitaActivity.this).inflate(R.layout.popup_company_manual, null);
        builder.setView(view);
        dialogEmpresa = builder.create();
        dialogEmpresa.show();

        final EditText nombre = view.findViewById(R.id.popupCompanyName);
        final EditText cif = view.findViewById(R.id.popupCompanyID);
        final EditText direccion = view.findViewById(R.id.popupCompanyLoc);
        final EditText poblacion = view.findViewById(R.id.popupCompanyPob);
        final EditText longitud = view.findViewById(R.id.popupCompanyLong);
        final EditText latitud = view.findViewById(R.id.popupCompanyLat);
        final EditText distancia = view.findViewById(R.id.popupCompanyDist);
        Button guardar = view.findViewById(R.id.popupSaveCompanyButton);


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!TextUtils.isEmpty(nombre.getText()) && !TextUtils.isEmpty(cif.getText()) && !TextUtils.isEmpty(direccion.getText()) && !TextUtils.isEmpty(poblacion.getText())
                            && !TextUtils.isEmpty(longitud.getText()) && !TextUtils.isEmpty(latitud.getText())) {

                        if (Double.parseDouble(distancia.getText().toString()) > 0) {
                            añadirEmpresa(nombre.getText().toString(), cif.getText().toString(), direccion.getText().toString(), poblacion.getText().toString(), longitud.getText().toString(), latitud.getText().toString(), distancia.getText().toString());
                        } else {
                            Toast.makeText(RealizarVisitaActivity.this, "La distancia debe ser positiva", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(RealizarVisitaActivity.this, "Error Añadiendo empresa", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
    }

    private void añadirEmpresa(String nombre, String cif, String direccion, String poblacion, String longitud, String latitud, String distancia) {
        try {
            Empresa empresa = new Empresa();
            empresa.setNombre(nombre);
            empresa.setCif(cif);
            empresa.setDireccion(direccion);
            empresa.setPoblacion(poblacion);
            empresa.setLongitud(Float.parseFloat(longitud));
            empresa.setLatitud(Float.parseFloat(latitud));
            empresa.setDistancia(Float.parseFloat(distancia));

            SharedPreferences prefs =
                    this.getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
            String address = prefs.getString("address", null);
            int port = prefs.getInt("port", -1);
            if (address != null && port != -1) {
                Settings settings = new Settings(address, port);
                empresaModel.addEmpresa(empresa, settings);

            } else {
                Toast.makeText(this, "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                        .show();
            }
            dialogEmpresa.dismiss();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La longitud, latitud y distancia deben ser números", Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al introducir la empresa", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
