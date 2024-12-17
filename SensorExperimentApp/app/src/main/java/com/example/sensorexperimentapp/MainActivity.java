package com.example.sensorexperimentapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer, proximity, light, rotationVector;
    private TextView tvSensorList, tvAccelerometerData, tvProximityData, tvLightData, tvOrientationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button detectSensorsButton = findViewById(R.id.btn_detectSensors);
        tvSensorList = findViewById(R.id.tv_sensorList);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        tvAccelerometerData = findViewById(R.id.tv_accelerometerData);
        tvProximityData = findViewById(R.id.tv_proximityData);
        tvLightData = findViewById(R.id.tv_lightData);
        tvOrientationData = findViewById(R.id.tv_orientation);

        detectSensorsButton.setOnClickListener(v -> {
            getSensorList();
        });

        // Initialize sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // Register listeners
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void getSensorList() {
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorInfo = new StringBuilder("Available Sensors:\n");
        for (Sensor sensor : sensorList) {
            sensorInfo.append(sensor.getName()).append(" (").append(sensor.getType()).append(")\n");
        }
        tvSensorList.setText(sensorInfo.toString());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            tvAccelerometerData.setText("Accelerometer Data: X: " + x + ", Y: " + y + ", Z: " + z);
        }
        else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            tvProximityData.setText("Proximity Data: " + event.values[0]);
        }
        else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            tvLightData.setText("Light Data: " + event.values[0]);
        }
        else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            float[] orientationAngles = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientationAngles);
            float azimuth = orientationAngles[0];
            float pitch = orientationAngles[1];
            float roll = orientationAngles[2];
            tvOrientationData.setText(
                "Orientation Data: " +
                "Azimuth: " + Math.toDegrees(azimuth) + ", " +
                "Pitch: " + Math.toDegrees(pitch) + ", " +
                "Roll: " + Math.toDegrees(roll)
            );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}