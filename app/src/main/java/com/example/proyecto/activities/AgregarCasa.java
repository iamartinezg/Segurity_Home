package com.example.proyecto.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyecto.R;
import com.example.proyecto.adapters.CasaAdapter;
import com.example.proyecto.databinding.ActivityAgregarCasaBinding;
import com.example.proyecto.modelos.Casa;
import com.example.proyecto.modelos.DatabaseRoutes;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AgregarCasa extends ActividadBasica {
    private ActivityAgregarCasaBinding binding;

    public static final String TAG = AgregarCasa.class.getName();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage mStorage = FirebaseStorage.getInstance();
    DatabaseReference mReference = mDatabase.getReference(DatabaseRoutes.CASAS_PATH);
    CasaAdapter adapter;

    ChildEventListener mValueEventListener;
    ArrayList<Casa> casas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_casa);
        binding = ActivityAgregarCasaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        adapter = new CasaAdapter(casas);
        binding.listacasas.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.listacasas.setLayoutManager(layoutManager);
        binding.listacasas.setAdapter(adapter);

        binding.botonAgregar.setOnClickListener(view -> {
            String Nombre = String.valueOf(binding.registerName);
            String Ubicacion = String.valueOf(binding.registerUbicacion);
            String Codigo = String.valueOf(binding.registerCodigo);
            String Role = String.valueOf(binding.registerRol);

            String key = mReference.push().getKey();
            DatabaseReference newCasaReference = mDatabase.getReference(DatabaseRoutes.getCasa(key));
            Casa casa = new Casa(Nombre, Ubicacion, Codigo, Role);
            newCasaReference.setValue(casa);
        });


        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Casa casa = child.getValue(Casa.class);
                    if (!casas.contains(casa)) casas.add(casa);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });

        mValueEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Casa casa = snapshot.getValue(Casa.class);
                if (!casas.contains(casa)) {
                    casas.add(casa);
                    adapter.notifyItemInserted(casas.size() - 1);
                    binding.listacasas.scrollToPosition(casas.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

    }
}
