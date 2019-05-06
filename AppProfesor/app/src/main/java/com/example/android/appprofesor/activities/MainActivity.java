package com.example.android.appprofesor.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.fragments.ClassListFragment;
import com.example.android.appprofesor.fragments.TutorListFragment;
import com.example.android.appprofesor.fragments.VisitListFragment;
import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.models.ClaseApercibimiento;
import com.example.android.appprofesor.models.TutorAlumno;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ClassListFragment.OnClassSelected, TutorListFragment.OnStudentSelected, VisitListFragment.OnStudentSelected {
    private int drawerOptionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        drawerOptionId = R.id.nav_apercibimientos;

        changeFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        drawerOptionId = item.getItemId();
        changeFragment();
        return true;
    }

    private void changeFragment() {
        Fragment fragment = null;

        if (drawerOptionId == R.id.nav_apercibimientos) {
            fragment = new ClassListFragment();
        } else if (drawerOptionId == R.id.nav_tutoria) {
            fragment = new TutorListFragment();
        } else if (drawerOptionId == R.id.nav_visitas) {
            fragment = new VisitListFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onChange(ClaseApercibimiento clase) {
        Intent intent = new Intent(MainActivity.this, ClassDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clase", clase);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onChange(TutorAlumno alumno) {
        Intent intent = new Intent(MainActivity.this, TutorDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("alumno", alumno);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onChange(Alumno alumno) {
        Intent intent = new Intent(MainActivity.this, VisitDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("alumno", alumno);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
