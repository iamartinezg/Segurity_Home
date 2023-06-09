package com.example.proyecto.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

;
import com.example.proyecto.R;

import com.example.proyecto.databinding.ActivityUbicacionBinding;
import com.example.proyecto.servicios.GPS_controler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ubicacion extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    Context context;

    Location location;
    GPS_controler gpsTracker;


    private static final int PERMISSION_REQUEST_LOCATION = 1;

    private ActivityUbicacionBinding binding;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUbicacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       ;

        // Obtener el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Configurar el mapa
        mapFragment.getMapAsync(this);
        gpsTracker = new GPS_controler((getApplicationContext()));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Verificar si se ha otorgado permiso para acceder a la ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Activar la capa de mi ubicación en el mapa
            mMap.setMyLocationEnabled(true);

            // Obtener la última ubicación conocida
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                // Mover la cámara a la ubicación del usuario
                LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                Utils.markersDeafault(mMap, getApplicationContext());
                mMap.setOnMapClickListener(this);
                mMap.setOnMarkerClickListener(this);

            }
        } else {
            // Solicitar permiso para acceder a la ubicación
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El usuario ha otorgado permiso para acceder a la ubicación
                // Ahora puedes obtener la ubicación del GPS
                onMapReady(mMap);
            } else {
                // El usuario ha denegado el permiso
                // Puedes mostrar un mensaje o realizar una acción adecuada en este caso
            }
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }
    public void AlertShow(String title, final LatLng latLng){
        AlertDialog.Builder builder= new AlertDialog.Builder(Ubicacion.this);

        builder.setMessage("Desea ir este punto?");
        builder.setTitle(title);
        builder.setCancelable(false);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.coordenadas.setDestinoLat(latLng.latitude);
                Utils.coordenadas.setDestinoLng(latLng.longitude);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        AlertShow(marker.getTitle(),marker.getPosition());
        return false;
    }
    private Boolean permissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == ((PackageManager.PERMISSION_GRANTED));
    }
    private void startInstalledAppDetailsActivity() {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


}