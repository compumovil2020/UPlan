package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Feed extends Fragment {

    ListView list;
    RelativeLayout layout;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    String[] nombre ={
            "Sara Rodriguez","David Ayala",
            "Luna Diaz","Camila Ruiz",
            "Diana Gonzales",
    };

    String[] descripcion ={
            "Tarde de parche en Andino ","Evento especial en Theatron",
            "Picnic en el Virrey","Subida al cerro de Monserrate",
            "Noche de fiesta en armando",
    };

    Integer[] imgevento={
            R.drawable.person_1,R.drawable.person_2,
            R.drawable.person_3,R.drawable.person_4,
            R.drawable.person_5,
    };

    Integer[] imgid={
            R.drawable.lugar_1,R.drawable.lugar_2,
            R.drawable.lugar_3,R.drawable.lugar_4,
            R.drawable.lugar_5,
    };

    Publicacion adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View feedView = inflater.inflate(R.layout.activity_feed, container, false);
        adapter = new Publicacion(this.getActivity(), nombre, descripcion,imgid,imgevento);
        list=(ListView)feedView.findViewById(R.id.list);
        list.setAdapter(adapter);
        layout=feedView.findViewById(R.id.layoutFeed);
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
        return feedView;
    }

    public void Invitaciones(View v){
        Intent intent = new Intent(v.getContext(),InvitationsActivity.class);
        startActivity(intent);
    }
    public void Feed(View v){
        Intent intent = new Intent(v.getContext(), Feed.class);
        startActivity(intent);
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