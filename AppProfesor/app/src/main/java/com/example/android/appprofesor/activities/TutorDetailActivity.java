package com.example.android.appprofesor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.fragments.ClassDetailFragment;
import com.example.android.appprofesor.fragments.TutorDetailFragment;
import com.example.android.appprofesor.models.ClaseApercibimiento;
import com.example.android.appprofesor.models.TutorAlumno;

/**
 * Muestra el fragment TutorDetailFragment con el alumno pasado por intent
 */
public class TutorDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail);

        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            TutorAlumno alumno = (TutorAlumno) bundle.getSerializable("alumno");
            TutorDetailFragment tdf = (TutorDetailFragment) getSupportFragmentManager().findFragmentById(R.id.tutorDetailsFragment);
            tdf.renderClass(alumno);
        }
    }
}
