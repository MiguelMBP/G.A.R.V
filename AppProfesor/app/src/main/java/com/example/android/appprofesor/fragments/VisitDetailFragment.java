package com.example.android.appprofesor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.Alumno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VisitDetailFragment extends Fragment {
    TextView alumno;
    TextView empresa;
    TextView localizacion;
    TextView coordenadas;
    TextView distancia;

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

        return view;
    }

    public void renderStudent(Alumno alumno) {
        this.alumno.setText(alumno.getNombre() + " " + alumno.getApellidos());
        empresa.setText(alumno.getEmpresa().getNombre());
        localizacion.setText(alumno.getEmpresa().getDireccion() + ", " + alumno.getEmpresa().getPoblación());
        coordenadas.setText(alumno.getEmpresa().getCoordenadaX() + ", " + alumno.getEmpresa().getCoordenadaY());
        //distancia.setText(alumno.getEmpresa().);
    }
}
