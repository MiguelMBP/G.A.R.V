package com.example.android.appprofesor.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.activities.MainActivity;

import androidx.fragment.app.Fragment;


public class InitialFragment extends Fragment {

    public InitialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial, container, false);

        Button apercibimientos = view.findViewById(R.id.btn_apercibimientos);
        Button tutor = view.findViewById(R.id.btn_tutor);
        Button visitas = view.findViewById(R.id.btn_visitas);
        Button contraseña = view.findViewById(R.id.btn_contraseña);

        apercibimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity a = (MainActivity) getContext();
                    a.setDrawerOptionId(R.id.nav_apercibimientos);
                } catch (Exception e) {
                    Log.e(InitialFragment.class.toString(), "Error");
                }
            }
        });
        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity a = (MainActivity) getContext();
                    a.setDrawerOptionId(R.id.nav_tutoria);
                } catch (Exception e) {
                    Log.e(InitialFragment.class.toString(), "Error");
                }
            }
        });
        visitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity a = (MainActivity) getContext();
                    a.setDrawerOptionId(R.id.nav_visitas);
                } catch (Exception e) {
                    Log.e(InitialFragment.class.toString(), "Error");
                }
            }
        });
        contraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity a = (MainActivity) getContext();
                    a.setDrawerOptionId(R.id.nav_cuenta);
                } catch (Exception e) {
                    Log.e(InitialFragment.class.toString(), "Error");
                }
            }
        });

        return view;
    }

}
