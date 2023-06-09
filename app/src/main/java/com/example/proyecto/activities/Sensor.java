package com.example.proyecto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivitySensorBinding;

public class Sensor extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
private ActivitySensorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        binding = ActivitySensorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.sensorTemp.setOnClickListener( view -> {
            Intent intent = new Intent(this, Temperature.class);
            startActivity(intent);
        });
        binding.sensorMove.setOnClickListener(view -> {
            Intent intent = new Intent(this, Proximity.class);
            startActivity(intent);
        });
    }
}