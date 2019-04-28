package com.example.android.appprofesor.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.activities.MapsActivity;
import com.example.android.appprofesor.models.Alumno;

import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class VisitDetailFragment extends Fragment {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Alumno alumno;

    TextView alumnoView;
    TextView empresa;
    TextView localizacion;
    TextView coordenadas;
    TextView distancia;
    Button verEnMapa;

    public VisitDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_detail, container, false);

        alumnoView = view.findViewById(R.id.textViewStudentVisitName);
        empresa = view.findViewById(R.id.textViewCompanyDetailName);
        localizacion = view.findViewById(R.id.textViewCompanyLoc);
        coordenadas = view.findViewById(R.id.textViewCompanyCoordinate);
        distancia = view.findViewById(R.id.textViewCompanyDistance);

        verEnMapa = view.findViewById(R.id.button_map);

        this.imageView = view.findViewById(R.id.textViewStudentVisitIcon);



        verEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("empresa", alumno.getEmpresa());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    public void renderStudent(Alumno alumno) {
        this.alumno = alumno;
        this.alumnoView.setText(alumno.getNombre() + " " + alumno.getApellidos());
        empresa.setText(alumno.getEmpresa().getNombre());
        localizacion.setText(alumno.getEmpresa().getDireccion() + ", " + alumno.getEmpresa().getPoblacion());
        coordenadas.setText(alumno.getEmpresa().getLatitud() + ", " + alumno.getEmpresa().getLongitud());
        distancia.setText(alumno.getEmpresa().getDistancia() + "");
    }
}
