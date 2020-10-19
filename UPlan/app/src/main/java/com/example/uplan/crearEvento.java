package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class crearEvento extends AppCompatActivity {

    Button perfil;
    Button feed;
    Button opciones;
    Button encuentros;
    TextView usuario, descrip, tema, anadirImagen;
    Button button5, button6;
    EditText editUsuario,editdescrip;
    Spinner spinner2;

    ConstraintLayout layout;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);
        encuentros = findViewById(R.id.encuentrosU);
        feed = findViewById(R.id.feedU);
        opciones = findViewById(R.id.optionsU);
        perfil = findViewById(R.id.perfilU);

        //Inflar elementos para el cambio de colores
        usuario = findViewById(R.id.usuario);
        final ColorStateList colorViejo = usuario.getTextColors();
        descrip = findViewById(R.id.descrip);
        tema = findViewById(R.id.tema);
        anadirImagen = findViewById(R.id.anadirImagen);
        final ColorStateList colorAnadir = anadirImagen.getTextColors();
        editUsuario = findViewById(R.id.editUsuario);
        editdescrip = findViewById(R.id.editdescrip);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        spinner2 = findViewById(R.id.spinner2);

        //Sensores de luminosidad
        layout= findViewById(R.id.layoutCrearEvento);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    usuario.setTextColor(getResources().getColor(R.color.blanco));
                    descrip.setTextColor(getResources().getColor(R.color.blanco));
                    tema.setTextColor(getResources().getColor(R.color.blanco));
                    anadirImagen.setTextColor(getResources().getColor(R.color.accentMorado));
                    editUsuario.setHintTextColor(getResources().getColor(R.color.blanco));
                    editUsuario.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editUsuario, ColorStateList.valueOf(Color.WHITE));
                    editdescrip.setHintTextColor(getResources().getColor(R.color.blanco));
                    editdescrip.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editdescrip, ColorStateList.valueOf(Color.WHITE));
                    button5.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    button6.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    button5.setTextColor(getResources().getColor(R.color.blanco));
                    button6.setTextColor(getResources().getColor(R.color.blanco));
                    spinner2.setBackgroundResource(R.color.blanco);
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    usuario.setTextColor(colorViejo);
                    descrip.setTextColor(colorViejo);
                    tema.setTextColor(colorViejo);
                    anadirImagen.setTextColor(colorAnadir);
                    editUsuario.setHintTextColor(colorViejo);
                    editUsuario.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editUsuario, colorViejo);
                    editdescrip.setHintTextColor(colorViejo);
                    editdescrip.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editdescrip, colorViejo);
                    button5.setBackgroundResource(R.drawable.botlogin);
                    button6.setBackgroundResource(R.drawable.botlogin);
                    button5.setTextColor(getResources().getColor(R.color.morado));
                    button6.setTextColor(getResources().getColor(R.color.morado));
                    spinner2.setBackgroundResource(R.color.blanco);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    public void cancelar(View v){
        this.finish();
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
