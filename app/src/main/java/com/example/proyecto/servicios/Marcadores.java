package com.example.proyecto.servicios;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.proyecto.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Marcadores {
    GoogleMap nMap;
    Context context;

    public Marcadores(GoogleMap nMap, Context context) {
        this.nMap = nMap;
        this.context = context;
    }

    public void addMarkersDefault (){
        uno(4.627292,  -74.069232, "CASA");


    }
    public void uno (Double latitud, Double longitud, String titulo){
        LatLng punto = new LatLng(latitud,longitud);
        int hight=140;
        int width=165;
        BitmapDrawable uno=(BitmapDrawable) context.getResources().getDrawable(R.drawable.imagencasa);
        Bitmap unos= uno.getBitmap();
        Bitmap uns=Bitmap.createScaledBitmap(unos,width,hight,false);
        nMap.addMarker(new MarkerOptions().position(punto).title(titulo).snippet("SECURITY HOME").icon(BitmapDescriptorFactory.fromBitmap(uns)));
    }
}
