package com.example.android.appprofesor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.fragments.VisitDetailFragment;
import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.models.TutorAlumno;

public class VisitDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_detail);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            Alumno alumno = (Alumno) bundle.getSerializable("alumno");
            VisitDetailFragment vdf = (VisitDetailFragment) getSupportFragmentManager().findFragmentById(R.id.visitDetailsFragment);
            vdf.renderStudent(alumno);

        }
    }
}
