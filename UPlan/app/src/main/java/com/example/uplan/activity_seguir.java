package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class activity_seguir extends AppCompatActivity {
    Button seguir;
    Button perfil;
    Button opciones;
    Button feed;
    Button encuentros;
    RelativeLayout layout;
    TextView textView3,textView4;
    Button post,myEvents;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguir);
        encuentros = findViewById(R.id.encuentrosU);
        feed = findViewById(R.id.feedU);
        opciones = findViewById(R.id.optionsU);
        perfil = findViewById(R.id.perfilU);
        //Inflar para colores
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        final ColorStateList colorViejo = textView3.getTextColors();
        seguir = findViewById(R.id.seguirUsuario);
        post = findViewById(R.id.post);
        myEvents = findViewById(R.id.myEvents);
        layout= findViewById(R.id.layoutSeguir);

        //Sensores de luminosidad
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    textView3.setTextColor(getResources().getColor(R.color.blanco));
                    textView4.setTextColor(getResources().getColor(R.color.blanco));
                    seguir.setBackgroundResource(R.color.morado_darkTheme);
                    post.setBackgroundResource(R.color.blanco);
                    myEvents.setBackgroundResource(R.color.blanco);
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    textView3.setTextColor(colorViejo);
                    textView4.setTextColor(colorViejo);
                    seguir.setBackgroundResource(R.color.morado);
                    post.setBackgroundResource(R.color.blanco);
                    myEvents.setBackgroundResource(R.color.blanco);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    public void seguirUnfollow (View v){
      Intent intent = new Intent(this,activity_unfollow.class);
      startActivity(intent);
    }
    public void Feed(View v){
        Intent intent = new Intent(v.getContext(), Feed.class);
        startActivity(intent);
    }
    public void Options(View v)
    {
        Intent intent = new Intent(v.getContext(),Options.class);
        startActivity(intent);
    }
    public void Profile(View v)
    {
        Intent intent = new Intent(v.getContext(),Profile.class);
        startActivity(intent);
    }
    public void Encuentros(View v)
    {
        Intent intent = new Intent(v.getContext(),EncuentrosActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
    }
}