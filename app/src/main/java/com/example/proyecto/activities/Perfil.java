package com.example.proyecto.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.proyecto.databinding.ActivityPerfilBinding;

public class Perfil extends AppCompatActivity {
    ActivityPerfilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
    }
}
