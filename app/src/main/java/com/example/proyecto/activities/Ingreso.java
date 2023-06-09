package com.example.proyecto.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityIngresoBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Ingreso extends ActividadBasica {
    ActivityIngresoBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIngresoBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(v -> doLogin());
        binding.signupButton.setOnClickListener(v -> startActivity(new Intent(Ingreso.this, Registro.class)));
        binding.forgotButton.setOnClickListener(v -> forgotPass());
    }

    public void doLogin() {
        if(binding.loginEmail.getEditText().getText().toString().isEmpty() ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.loginEmail.getEditText().getText().toString()).matches()){
            binding.loginEmail.setError(getString(R.string.mail_error_label));
            return;
        } else binding.loginEmail.setErrorEnabled(false);
        if (binding.loginPass.getEditText().getText().toString().isEmpty()) {
            binding.loginPass.setError(getString(R.string.error_pass_label));
            return;
        } else binding.loginPass.setErrorEnabled(false);

        auth.signInWithEmailAndPassword(binding.loginEmail.getEditText().getText().toString(),
                binding.loginPass.getEditText().getText().toString()).addOnFailureListener(e -> {
            alerts.indefiniteSnackbar(binding.getRoot(), e.getLocalizedMessage());
        });
        Intent intent = new Intent(this, Servicio.class);
        startActivity(intent);
    }

    private void forgotPass(){
        if(binding.loginEmail.getEditText().getText().toString().isEmpty() ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.loginEmail.getEditText().getText().toString()).matches()){
            binding.loginEmail.setError(getString(R.string.mail_error_label));
            return;
        } else binding.loginEmail.setErrorEnabled(false);
        auth.sendPasswordResetEmail(binding.loginEmail.getEditText().getText().toString())
                .addOnSuccessListener(aVoid -> {
                    alerts.indefiniteSnackbar(binding.getRoot(), "Revise su correo");
                }).addOnFailureListener(e -> {
                    alerts.indefiniteSnackbar(binding.getRoot(), e.getLocalizedMessage());
                });
    }
}