package com.example.android.appprofesor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.fragments.ClassDetailFragment;
import com.example.android.appprofesor.models.ClaseApercibimiento;


public class ClassDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            ClaseApercibimiento clase = (ClaseApercibimiento) bundle.getSerializable("clase");
            ClassDetailFragment cdf = (ClassDetailFragment) getSupportFragmentManager().findFragmentById(R.id.ClassDetailsFragment);
            cdf.renderClass(clase);
        }
    }
}
