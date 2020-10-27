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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

public class EncuentrosActivity extends Fragment {

    ListView list;
    Button invitations;
    ConstraintLayout layout;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    private PuntoEncuentro adapter;

    String[] nombre ={
            "El pre de la fiestaaa","Let's party",
            "Lunada","Salida cristiana por los niños",
            "Reunion uniJave",
    };

    String[] evento = {
            "Theatron","Bruno","Guatavita","Club Militar","Universidad Javeriana",
    };

    String[] fecha ={
            "25/03/2021","25/03/2021","25/03/2021","25/03/2021","25/03/2021","25/03/2021",
    };

    Integer[] imgperfil={
            R.drawable.person_1,R.drawable.person_2,
            R.drawable.person_3,R.drawable.person_4,
            R.drawable.person_5,
    };

    LatLng[] position={
            new LatLng(4.624335, -74.063644),
            new LatLng(4.824335, -75.063644),
            new LatLng(4.624335, -76.063644),
            new LatLng(4.634335, -70.063644),
            new LatLng(5.624335, -78.063644),
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View encuentrosView = inflater.inflate(R.layout.activity_encuentros, container, false);
        layout=encuentrosView.findViewById(R.id.layoutEncuentros);
        adapter = new PuntoEncuentro(this.getActivity(), nombre, evento, fecha,imgperfil,position);
        list=(ListView)encuentrosView.findViewById(R.id.lista);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l)
            {
                PuntoEncuentro punto = (PuntoEncuentro) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext() , eventoMapa.class);
                intent.putExtra("codigo", 2);
                startActivity(intent);
            }
        });
        invitations = (Button) encuentrosView.findViewById(R.id.invitations);
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    layout.setBackgroundResource(R.color.dark_bg);
                } else {
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