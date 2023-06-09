package com.example.proyecto.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityMainBinding;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends Autenticacion {

    private ActivityMainBinding binding;

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.botonIngresar.setOnClickListener(view -> {
            botonIngresar();
        });
        binding.botonRegistrar.setOnClickListener(view -> {
            botonRegistrar();
        });
    }
    public void botonIngresar(){
        Intent intent = new Intent(this, Ingreso.class);
        startActivity(intent);
    }

    public void botonRegistrar(){
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}