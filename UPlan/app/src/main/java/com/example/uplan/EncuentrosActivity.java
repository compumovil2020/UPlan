package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EncuentrosActivity extends Fragment {

    Button invitations, feed, opciones, perfil;
    ConstraintLayout layout;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View encuentrosView = inflater.inflate(R.layout.activity_encuentros, container, false);
        invitations = (Button) encuentrosView.findViewById(R.id.invitations);
        layout=encuentrosView.findViewById(R.id.layoutEncuentros);
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        invitations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),InvitationsActivity.class);
                startActivity(intent);
            }
        });
        return encuentrosView;
    }


    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
    }

}