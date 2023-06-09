package com.example.proyecto.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityNotificacionesBinding;

public class Notificaciones extends AppCompatActivity {

    private ActivityNotificacionesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        binding = ActivityNotificacionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}