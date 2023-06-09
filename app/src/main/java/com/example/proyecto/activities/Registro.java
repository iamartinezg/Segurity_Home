package com.example.proyecto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityRegistroBinding;
import com.example.proyecto.modelos.DatabaseRoutes;
import com.example.proyecto.modelos.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends ActividadBasica {
    ActivityRegistroBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.registerButton.setOnClickListener(v ->register());
    }

    private void register(){
        if(binding.registerEmail.getEditText().getText().toString().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(binding.registerEmail.getEditText().getText().toString()).matches()){
            binding.registerEmail.setError(getString(R.string.mail_error_label));
            return;
        } else binding.registerPass.setErrorEnabled(false);
        if (binding.registerPass.getEditText().getText().toString().isEmpty()) {
            binding.registerPass.setError(getString(R.string.error_pass_label));
            return;
        } else binding.registerPass.setErrorEnabled(false);
        if (binding.registerFullName.getEditText().getText().toString().isEmpty()) {
            binding.registerFullName.setError(getString(R.string.error_pass_label));
            return;
        } else binding.registerFullName.setErrorEnabled(false);
        if (binding.registerPhone.getEditText().getText().toString().isEmpty()) {
            binding.registerPhone.setError(getString(R.string.error_pass_label));
            return;
        } else binding.registerPhone.setErrorEnabled(false);

        auth.createUserWithEmailAndPassword(binding.registerEmail.getEditText().getText().toString(),
                        binding.registerPass.getEditText().getText().toString())
                .addOnSuccessListener(authResult -> {
                    //TO_DO: Save user data
                    String name = binding.registerFullName.getEditText().getText().toString();
                    long phone = Long.parseLong(binding.registerPhone.getEditText().getText().toString());
                    String mail = binding.registerEmail.getEditText().getText().toString();
                    User usuario = new User(name, mail, phone);

                    DatabaseReference reference = database.getReference(DatabaseRoutes.getUser(auth.getCurrentUser().getUid()));
                    reference.setValue(usuario).addOnSuccessListener(aVoid ->{
                    Intent intent = new Intent(this, Servicio.class);
                    startActivity(intent);
                    }).addOnFailureListener(e -> {
                    alerts.indefiniteSnackbar(binding.getRoot(), e.getLocalizedMessage());});
                });
    }
}