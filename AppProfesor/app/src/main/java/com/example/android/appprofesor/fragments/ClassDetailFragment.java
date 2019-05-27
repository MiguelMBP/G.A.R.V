package com.example.android.appprofesor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.adapters.ClassStudentAdapter;
import com.example.android.appprofesor.models.AlumnoApercibimiento;
import com.example.android.appprofesor.models.ClaseApercibimiento;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment para mostrar la vista detallada de un objeto ClaseApercibimiento
 */
public class ClassDetailFragment extends Fragment {
    TextView clase;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ClassStudentAdapter adapter;
    List<AlumnoApercibimiento> alumnos = new ArrayList<>();


    public ClassDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_detail, container, false);

        clase = view.findViewById(R.id.textViewSubjectClass);

        recyclerView = view.findViewById(R.id.class_student_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ClassStudentAdapter(alumnos, R.layout.class_student_row, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Actualiza la interfaz con los datos del objeto
     * @param clase
     */
    public void renderClass(ClaseApercibimiento clase) {
        alumnos = clase.getAlumnos();

        this.clase.setText(clase.getUnidad() + " - " + clase.getMateria());

        adapter = new ClassStudentAdapter(alumnos, R.layout.class_student_row, getContext());
        recyclerView.setAdapter(adapter);
    }
}
