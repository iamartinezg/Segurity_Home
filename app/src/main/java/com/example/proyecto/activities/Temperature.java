package com.example.proyecto.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.hardware.Sensor;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto.R;

import java.util.Timer;
import java.util.TimerTask;

import com.example.proyecto.databinding.ActivitySensorBinding;
import com.example.proyecto.databinding.TemperatureSensorBinding;

public class Temperature extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TemperatureActivity termometro;
    private float temperatura;
    private Timer timer;
    private ActivitySensorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_sensor);
        getSupportActionBar().hide();
        termometro= (TemperatureActivity) findViewById(R.id.temperatura);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //loadAmbienteTemperature();
        simulateAmbientTemperature();
    }
    @Override
    protected  void onPause(){
        super.onPause();
        unregisterAll();
    }

    private void simulateAmbientTemperature(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                temperatura= Utils.randInt(-10,35);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        termometro.setCurrentTemp(temperatura);
                        getSupportActionBar().setTitle(getString(R.string.app_name)+ " : "+ temperatura);
                    }
                });
            }
        },0, 3500);
    }

    private void loadAmbienteTemperature(){
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }else {
            Toast.makeText(this,"No existe sensor de ambiente de temperatura!", Toast.LENGTH_SHORT).show();
        }
    }
    private void unregisterAll(){
        //sensorManager.unregisterListener(this);
        timer.cancel();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        if(sensorEvent.values.length > 0){
            temperatura = sensorEvent.values[0];
            termometro.setCurrentTemp(temperatura);
            getSupportActionBar().setTitle(getString(R.string.app_name)+ " : "+ temperatura);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }
}
