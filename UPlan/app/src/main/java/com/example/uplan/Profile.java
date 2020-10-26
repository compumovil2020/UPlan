package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Profile extends Fragment {


    TextView textView3,textView4;
    Button encuentros,feed,opciones,perfil, crEvento, edPerfil,post,myEvents;
    RelativeLayout layout;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.activity_profile, container, false);
        encuentros = profileView.findViewById(R.id.encuentrosU);
        feed = profileView.findViewById(R.id.feedU);
        opciones = profileView.findViewById(R.id.optionsU);
        perfil = profileView.findViewById(R.id.perfilU);
        textView3 = profileView.findViewById(R.id.textView3);
        textView4 = profileView.findViewById(R.id.textView4);
        final ColorStateList colorViejo=textView3.getTextColors();
        crEvento = profileView.findViewById(R.id.crearEvento);
        edPerfil = profileView.findViewById(R.id.editarPerfil);
        post = profileView.findViewById(R.id.post);
        myEvents = profileView.findViewById(R.id.myEvents);
        layout=profileView.findViewById(R.id.layoutProfile);
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    textView3.setTextColor(getResources().getColor(R.color.blanco));
                    textView4.setTextColor(getResources().getColor(R.color.blanco));
                    edPerfil.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    crEvento.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    edPerfil.setTextColor(getResources().getColor(R.color.blanco));
                    crEvento.setTextColor(getResources().getColor(R.color.blanco));
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    textView3.setTextColor(colorViejo);
                    textView4.setTextColor(colorViejo);
                    edPerfil.setBackgroundResource(R.drawable.botlogin);
                    crEvento.setBackgroundResource(R.drawable.botlogin);
                    edPerfil.setTextColor(getResources().getColor(R.color.morado));
                    crEvento.setTextColor(getResources().getColor(R.color.morado));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        crEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListaEventos.class);
                startActivity(intent);
            }
        });

        edPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), editarPerfil.class);
                startActivity(intent);
            }
        });

        return profileView;

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