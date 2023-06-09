package com.example.proyecto.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.proyecto.adapters.ContactosAdapter;
import com.example.proyecto.databinding.ActivityContactosBinding;
import com.example.proyecto.servicios.PermisosServicios;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Contactos extends ActividadBasica {

    private ActivityContactosBinding binding;
    @Inject
    PermisosServicios permisosServicios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        permisosServicios.getContactsPermission(this);

        if(permisosServicios.ismContactsPermissionGranted()) initContacts();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permisosServicios.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (permisosServicios.ismContactsPermissionGranted()) {
                initContacts();
            }
        }
    }
    public void initContacts(){
        binding.contactsListView.setAdapter(
                new ContactosAdapter(this, getContentResolver().query(android.provider.ContactsContract.Contacts.CONTENT_URI, ContactosAdapter.PROJECTION, ContactosAdapter.FILTER, null, ContactosAdapter.ORDER), 0));
    }
}