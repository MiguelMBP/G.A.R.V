package com.example.android.appprofesor.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.Alumno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class VisitDetailFragment extends Fragment {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    TextView alumno;
    TextView empresa;
    TextView localizacion;
    TextView coordenadas;
    TextView distancia;
    Button confirmarVisita;
    Camera camera;

    public VisitDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_detail, container, false);

        alumno = view.findViewById(R.id.textViewStudentVisitName);
        empresa = view.findViewById(R.id.textViewCompanyDetailName);
        localizacion = view.findViewById(R.id.textViewCompanyLoc);
        coordenadas = view.findViewById(R.id.textViewCompanyCoordinate);
        distancia = view.findViewById(R.id.textViewCompanyDistance);
        confirmarVisita = view.findViewById(R.id.button_visit);

        this.imageView = view.findViewById(R.id.textViewStudentVisitIcon);

        confirmarVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        return view;
    }

    public void renderStudent(Alumno alumno) {
        this.alumno.setText(alumno.getNombre() + " " + alumno.getApellidos());
        empresa.setText(alumno.getEmpresa().getNombre());
        localizacion.setText(alumno.getEmpresa().getDireccion() + ", " + alumno.getEmpresa().getPoblación());
        coordenadas.setText(alumno.getEmpresa().getCoordenadaX() + ", " + alumno.getEmpresa().getCoordenadaY());
        //distancia.setText(alumno.getEmpresa().);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

        public void onActivityResult (int requestCode, int resultCode, Intent data){
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
            }
        }


}
