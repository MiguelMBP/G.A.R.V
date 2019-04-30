package com.example.android.appprofesor.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.Empresa;
import com.example.android.appprofesor.utils.Constants;
import com.example.android.appprofesor.viewmodels.EmpresaViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class SelectMapActivity extends FragmentActivity implements OnMapReadyCallback, Constants {

    private GoogleMap mMap;
    private AlertDialog.Builder builder;
    private AlertDialog dialogEmpresa;
    EmpresaViewModel empresaModel;
    String parsedDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        empresaModel = ViewModelProviders.of(this).get(EmpresaViewModel.class);
        setContentView(R.layout.activity_select_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng instituto = new LatLng(SCHOOLAT, SCHOOLON);
        mMap.addMarker(new MarkerOptions().position(instituto).title("IES Fernando Aguilar Quignon"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(instituto));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng clickPos) {
                try {
                    Geocoder geocoder = new Geocoder(SelectMapActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(clickPos.latitude, clickPos.longitude, 1);
                    createPopUpEmpresa(addresses);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createPopUpEmpresa(List<Address> addresses) {
        builder = new AlertDialog.Builder(SelectMapActivity.this);
        View view = LayoutInflater.from(SelectMapActivity.this).inflate(R.layout.popup_company, null);
        builder.setView(view);
        dialogEmpresa = builder.create();
        dialogEmpresa.show();

        final EditText nombre = view.findViewById(R.id.popupCompanyName);
        final EditText cif = view.findViewById(R.id.popupCompanyID);
        final EditText direccion = view.findViewById(R.id.popupCompanyLoc);
        final EditText poblacion = view.findViewById(R.id.popupCompanyPob);
        final EditText longitud = view.findViewById(R.id.popupCompanyLong);
        final EditText latitud = view.findViewById(R.id.popupCompanyLat);
        Button guardar = view.findViewById(R.id.popupSaveCompanyButton);

        direccion.setText(addresses.get(0).getThoroughfare());
        poblacion.setText(addresses.get(0).getLocality());
        longitud.setText(addresses.get(0).getLongitude() + "");
        latitud.setText(addresses.get(0).getLatitude() + "");

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!TextUtils.isEmpty(nombre.getText()) && !TextUtils.isEmpty(cif.getText()) && !TextUtils.isEmpty(direccion.getText()) && !TextUtils.isEmpty(poblacion.getText())
                            && !TextUtils.isEmpty(longitud.getText()) && !TextUtils.isEmpty(latitud.getText())) {
                        añadirEmpresa(nombre.getText().toString(), cif.getText().toString(), direccion.getText().toString(), poblacion.getText().toString(), longitud.getText().toString(), latitud.getText().toString());
                    }
                } catch (Exception e) {
                    Toast.makeText(SelectMapActivity.this, "Error Añadiendo empresa", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
    }

    private void añadirEmpresa(String nombre, String cif, String direccion, String poblacion, String longitud, String latitud) {
        Empresa empresa = new Empresa();
        empresa.setNombre(nombre);
        empresa.setCif(cif);
        empresa.setDireccion(direccion);
        empresa.setPoblacion(poblacion);
        empresa.setLongitud(Float.parseFloat(longitud));
        empresa.setLatitud(Float.parseFloat(latitud));
        empresa.setDistancia(getDistance(Double.parseDouble(latitud), Double.parseDouble(longitud)));

        empresaModel.addEmpresa(empresa);

        dialogEmpresa.dismiss();
    }

    public float getDistance(final double lat1, final double lon1){

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + SCHOOLAT + "," + SCHOOLON + "&sensor=false&units=metric&mode=driving&key=" + getResources().getString(R.string.google_maps_key));
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String response = iStreamToString(in);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance=distance.getString("value");

                    Log.v("Distsnce","Distance>>"+parsedDistance);

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Float.parseFloat(parsedDistance)/1000f;
    }


    public String iStreamToString(InputStream is1)
    {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is1), 4096);
        String line;
        StringBuilder sb =  new StringBuilder();
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String contentOfMyInputStream = sb.toString();
        return contentOfMyInputStream;
    }
}
