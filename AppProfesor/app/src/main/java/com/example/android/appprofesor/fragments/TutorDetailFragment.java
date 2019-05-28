package com.example.android.appprofesor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.adapters.TutorAdapter;
import com.example.android.appprofesor.adapters.WarningAdapter;
import com.example.android.appprofesor.models.TutorAlumno;
import com.example.android.appprofesor.models.TutorAsignatura;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment para mostrar la vista detallada de un objeto TutorAlumno
 */
public class TutorDetailFragment extends Fragment {
    private TextView curso;
    private TextView nombre;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private WarningAdapter adapter;
    private List<TutorAsignatura> asignaturas = new ArrayList<>();

    public TutorDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_detail, container, false);

        curso = view.findViewById(R.id.textViewClassTutorName);
        nombre = view.findViewById(R.id.textViewStudentTutorName);

        recyclerView = view.findViewById(R.id.recyclerTutorWarning);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new WarningAdapter(asignaturas, R.layout.warning_row, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Actualiza la interfaz con los datos del alumno
     * @param alumno
     */
    public void renderClass(TutorAlumno alumno) {
        asignaturas = alumno.getAsignaturas();
        adapter = new WarningAdapter(asignaturas, R.layout.warning_row, getContext());
        recyclerView.setAdapter(adapter);

        curso.setText(alumno.getCurso());
        nombre.setText(alumno.getNombre());
    }
}
