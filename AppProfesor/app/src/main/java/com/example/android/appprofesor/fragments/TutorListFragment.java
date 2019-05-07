package com.example.android.appprofesor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.adapters.TutorAdapter;
import com.example.android.appprofesor.models.ClaseApercibimiento;
import com.example.android.appprofesor.models.Settings;
import com.example.android.appprofesor.models.TutorAlumno;
import com.example.android.appprofesor.utils.Utils;
import com.example.android.appprofesor.viewmodels.ClaseViewModel;
import com.example.android.appprofesor.viewmodels.SettingsViewModel;
import com.example.android.appprofesor.viewmodels.TutorAlumnoViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TutorListFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TutorAdapter adapter;
    List<TutorAlumno> alumnos = new ArrayList<>();
    TutorAlumnoViewModel model;

    OnStudentSelected callback;

    public TutorListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_list, container, false);

        recyclerView = view.findViewById(R.id.class_student_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //alumnos = Utils.getDummyStudent();

        adapter = new TutorAdapter(alumnos, R.layout.tutor_row, getContext(), new TutorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TutorAlumno alumno, int position) {
                callback.onChange(alumno);
            }
        });

        recyclerView.setAdapter(adapter);

        model = ViewModelProviders.of(this).get(TutorAlumnoViewModel.class);
        try {
            SharedPreferences prefs =
                    getContext().getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
            String address = prefs.getString("address", null);
            int port = prefs.getInt("port", -1);
            if (address != null && port != -1) {
                Settings settings = new Settings(address, port);
                model.getAlumnos(getContext(), settings).observe(this, new Observer<List<TutorAlumno>>() {

                    @Override
                    public void onChanged(List<TutorAlumno> tutorAlumnos) {
                        adapter.addAlumnos(tutorAlumnos);
                    }
                });
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                    .show();
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (OnStudentSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public interface OnStudentSelected {
        public void onChange(TutorAlumno alumno);
    }
}
