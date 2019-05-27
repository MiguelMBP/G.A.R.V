package com.example.android.appprofesor.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.android.appprofesor.models.Settings;
import com.example.android.appprofesor.utils.Constants;
import com.example.android.appprofesor.viewmodels.EmpresaViewModel;
import com.example.android.appprofesor.viewmodels.SettingsViewModel;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Muestra un mapa para seleccionar una ubicación, recoge los datos de esa ubicación y muestra un dialogo para crear una empresa
 */
public class SelectMapActivity extends FragmentActivity implements OnMapReadyCallback, Constants {

    private GoogleMap mMap;
    AutocompleteFragment autocompleteFragment;
    private AlertDialog.Builder builder;
    private AlertDialog dialogEmpresa;
    EmpresaViewModel empresaModel;
    String parsedDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_map);

        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));

        autocompleteFragment = (AutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                LatLng position = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                mMap.addMarker(new MarkerOptions().position(position).title(place.getName()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14.0f));

            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });
        empresaModel = ViewModelProviders.of(this).get(EmpresaViewModel.class);
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(instituto, 10.0f));

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

        SharedPreferences prefs =
                this.getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
        String address = prefs.getString("address", null);
        int port = prefs.getInt("port", -1);
        if (address != null && port != -1) {
            Settings settings = new Settings(address, port);
            empresaModel.addEmpresa(empresa, settings);
        } else {
            Toast.makeText(this, "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                    .show();
        }
        dialogEmpresa.dismiss();
    }

    private float getDistance(final double lat1, final double lon1) {

        Thread thread = new Thread(new Runnable() {
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
                    parsedDistance = distance.getString("value");

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
        return Float.parseFloat(parsedDistance) / 1000f;
    }


    public String iStreamToString(InputStream is1) {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is1), 4096);
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String contentOfMyInputStream = sb.toString();
        return contentOfMyInputStream;
    }
}
