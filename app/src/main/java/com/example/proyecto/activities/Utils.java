package com.example.proyecto.activities;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.proyecto.servicios.Coordenadas;
import com.example.proyecto.servicios.Marcadores;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Utils {
    public static final Random RANDOM  = new Random();

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp *((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
    public static int randInt (int min, int max) {
        int randomNum = RANDOM.nextInt((max-min)+1)+min;
        return randomNum;
    }
    public static Coordenadas coordenadas=new Coordenadas();
    public static List<HashMap<String,String>> routes=new ArrayList<>();
    public static void markersDeafault(GoogleMap nMap, Context context){
        new Marcadores(nMap, context).addMarkersDefault();
    }
    public static Coordenadas coordenada=new Coordenadas();
}
