package com.example.android.appprofesor.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.activities.RealizarVisitaActivity;
import com.example.android.appprofesor.adapters.VisitAdapter;
import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.models.Settings;
import com.example.android.appprofesor.models.TutorAlumno;
import com.example.android.appprofesor.utils.Utils;
import com.example.android.appprofesor.viewmodels.SettingsViewModel;
import com.example.android.appprofesor.viewmodels.TutorAlumnoViewModel;
import com.example.android.appprofesor.viewmodels.VisitaViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VisitListFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    VisitAdapter adapter;
    List<Alumno> alumnos = new ArrayList<>();

    OnStudentSelected callback;
    VisitaViewModel model;
    private List<String> myData;


    public VisitListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_list, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_visitas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), RealizarVisitaActivity.class);
                startActivity(intent);

            }
        });

        FloatingActionButton fabAct = (FloatingActionButton) view.findViewById(R.id.fab_act_visitas);
        fabAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences prefs =
                            getContext().getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
                    String address = prefs.getString("address", null);
                    int port = prefs.getInt("port", -1);
                    if (address != null && port != -1) {
                        Settings settings = new Settings(address, port);
                        model = ViewModelProviders.of(VisitListFragment.this).get(VisitaViewModel.class);
                        model.getAlumnos(getContext(), settings).observe(VisitListFragment.this, new Observer<List<Alumno>>() {


                            @Override
                            public void onChanged(List<Alumno> alumnos) {
                                adapter.addAlumnos(alumnos);
                            }
                        });
                    } else {
                        throw new NullPointerException();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(getContext(), "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        recyclerView = view.findViewById(R.id.visit_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //alumnos = Utils.getDummyVisit();

        adapter = new VisitAdapter(alumnos, R.layout.visit_row, getContext(), new VisitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Alumno alumno, int position) {
                callback.onChange(alumno);
            }
        });
        recyclerView.setAdapter(adapter);

        model = ViewModelProviders.of(this).get(VisitaViewModel.class);
        try {
            SharedPreferences prefs =
                    getContext().getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
            String address = prefs.getString("address", null);
            int port = prefs.getInt("port", -1);
            if (address != null && port != -1) {
                Settings settings = new Settings(address, port);
                model.getAlumnos(getContext(), settings).observe(this, new Observer<List<Alumno>>() {
                    @Override
                    public void onChanged(List<Alumno> alumnos) {
                        adapter.addAlumnos(alumnos);
                    }
                });
            }else {
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
        public void onChange(Alumno alumno);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            SharedPreferences prefs =
                    getContext().getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
            String address = prefs.getString("address", null);
            int port = prefs.getInt("port", -1);
            if (address != null && port != -1) {
                Settings settings = new Settings(address, port);
                model.getAlumnos(getContext(), settings).observe(this, new Observer<List<Alumno>>() {


                    @Override
                    public void onChanged(List<Alumno> alumnos) {
                        adapter.addAlumnos(alumnos);
                    }
                });
            }else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                    .show();
        }
    }


}
