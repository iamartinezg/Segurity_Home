package com.example.proyecto.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyecto.adapters.MensajeAdapter;
import com.example.proyecto.activities.Autenticacion;
import com.example.proyecto.databinding.ActivityChatFamiliarBinding;
import com.example.proyecto.modelos.DatabaseRoutes;
import com.example.proyecto.modelos.Mensaje;
import com.example.proyecto.servicios.CameraService;
import com.example.proyecto.servicios.NotificationService;
import com.example.proyecto.servicios.PermisosServicios;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class ChatFamiliar extends ActividadBasica {

    private ActivityChatFamiliarBinding binding;
    public static final String TAG = ChatFamiliar.class.getName();


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Inject
    CameraService cameraService;

    @Inject
    PermisosServicios permissionHelper;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage mStorage = FirebaseStorage.getInstance();
    DatabaseReference mReference = mDatabase.getReference(DatabaseRoutes.MESSAGES_PATH);
    ChildEventListener mValueEventListener;
    MensajeAdapter adapter;

    List<Mensaje> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatFamiliarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        adapter = new MensajeAdapter(messages);
        binding.chatMessages.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatMessages.setLayoutManager(layoutManager);
        binding.chatMessages.setAdapter(adapter);

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Mensaje message = child.getValue(Mensaje.class);
                    if (!messages.contains(message))
                        messages.add(message);
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
                Mensaje message = snapshot.getValue(Mensaje.class);
                if (!messages.contains(message)) {
                    messages.add(message);
                    adapter.notifyItemInserted(messages.size() - 1);
                    binding.chatMessages.scrollToPosition(messages.size() - 1);
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


        //Message event
        binding.sendButton.setOnClickListener(view -> {
            String message = binding.messageEdit.getEditText().getText().toString();
            if (!message.isEmpty()) {
                binding.sendButton.playAnimation();
                binding.sendButton.addAnimatorListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.messageEdit.getEditText().getText().clear();
                        String key = mReference.push().getKey();
                        DatabaseReference newMessageReference = mDatabase.getReference(DatabaseRoutes.getMessage(key));
                        Mensaje msgToSend = new Mensaje(key, message, false, null, Autenticacion.auth.getCurrentUser().getEmail(), new Date().getTime());
                        newMessageReference.setValue(msgToSend);
                    }
                });
            } else {
                binding.messageEdit.setError("El mensaje no puede estar vacio.");
            }
        });
        //Camera Event
        binding.sendImageButton.setOnClickListener(view -> {
            if (!binding.messageEdit.getEditText().getText().toString().isEmpty()) {
                if (permissionHelper.isMCameraPermissionGranted()) {
                    cameraService.startCamera(this);
                } else {
                    permissionHelper.getContactsPermission(this);
                    alerts.shortSimpleSnackbar(binding.getRoot(), "Looks like we don't have camera permission.");
                }
            } else {
                binding.messageEdit.setError("El mensaje no puede estar vacio.");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReference.removeEventListener(mValueEventListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PermisosServicios.PERMISSIONS_REQUEST_CAMERA:
                    binding.sendImageButton.playAnimation();
                    StorageReference mStorageReference = mStorage.getReference(DatabaseRoutes.getMessage(cameraService.getPhotoName()));
                    mStorageReference.putFile(cameraService.getPhotoURI()).addOnSuccessListener(taskSnapshot -> {
                        mStorageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String key = mReference.push().getKey();
                            DatabaseReference newMessageReference = mDatabase.getReference(DatabaseRoutes.getMessage(key));
                            Mensaje msgToSend = new Mensaje(key, binding.messageEdit.getEditText().getText().toString(), true, uri.toString(), Autenticacion.auth.getCurrentUser().getEmail(), new Date().getTime());
                            newMessageReference.setValue(msgToSend);
                        }).addOnFailureListener(e -> e.printStackTrace());
                    }).addOnFailureListener(e -> e.printStackTrace());
                    break;
            }
        }
    }
}