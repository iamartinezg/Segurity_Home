package com.example.proyecto.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityServicioBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Servicio extends AppCompatActivity {

    private ActivityServicioBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio);
        binding = ActivityServicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.botonCamara.setOnClickListener(view -> {
            Intent intent = new Intent(this, Camaras.class);
            startActivity(intent);
        });
        binding.botonCasa.setOnClickListener(view -> {
            Intent intent = new Intent(this, AgregarCasa.class);
            startActivity(intent);
        });
        binding.botonSensores.setOnClickListener(view -> {
            Intent intent = new Intent(this, Sensor.class);
            startActivity(intent);
        });
        binding.botonContactos.setOnClickListener(view -> startActivity(new Intent(this, Contactos.class)));

        binding.botonUbicacion.setOnClickListener(view -> {
            Intent intent = new Intent(this, Ubicacion.class);
            startActivity(intent);
        });
        binding.botonNotificaciones.setOnClickListener(view -> {
            Intent intent = new Intent(this, Notificaciones.class);
            startActivity(intent);
        });
        binding.logo.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatFamiliar.class);
            startActivity(intent);
        });
    }
}