package com.example.proyecto.activities;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.hardware.Sensor;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.proyecto.R;

public class Proximity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private TextView proximityValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proximity_sensor);
        getSupportActionBar().hide();
        proximityValue = (TextView) findViewById(R.id.proximityValue);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (proximitySensor == null) {
            proximityValue.setText("Proximity sensor not available");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximityValue.setText(String.valueOf(event.values[0]));
            if (event.values[0] < proximitySensor.getMaximumRange()) {
                // Cambiar el color de fondo a rojo
                ConstraintLayout layout = findViewById(R.id.relativeLayout);
                layout.setBackgroundColor(getResources().getColor(R.color.rojo));
            } else{
                ConstraintLayout layout = findViewById(R.id.relativeLayout);
                layout.setBackground(getDrawable(R.drawable.fondo_degrado));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No es necesario implementar esto en este ejemplo.
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
// Detener la escucha del sensor de proximidad cuando la actividad esté en pausa
        sensorManager.unregisterListener(this);
    }
}