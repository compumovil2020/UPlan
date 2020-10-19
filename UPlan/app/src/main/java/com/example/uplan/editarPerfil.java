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
import android.widget.TextView;

public class editarPerfil extends AppCompatActivity {

    TextView anadirFoto,textView6,textView9;
    EditText editTextTextMultiline,textView8;
    Button button5,button6;
    ConstraintLayout layout;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_peril);

        layout= findViewById(R.id.layoutEditarPerfil);
        anadirFoto=findViewById(R.id.anadirFoto);
        textView6=findViewById(R.id.textView6);
        textView9=findViewById(R.id.textView9);
        final ColorStateList colorViejo = textView6.getTextColors();
        editTextTextMultiline=findViewById(R.id.editTextTextMultiLine);
        textView8=findViewById(R.id.textView8);
        button5=findViewById(R.id.button5);
        button6=findViewById(R.id.button6);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    anadirFoto.setTextColor(getResources().getColor(R.color.accentMorado));
                    textView6.setTextColor(getResources().getColor(R.color.blanco));
                    textView9.setTextColor(getResources().getColor(R.color.blanco));
                    editTextTextMultiline.setHintTextColor(getResources().getColor(R.color.blanco));
                    editTextTextMultiline.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editTextTextMultiline, ColorStateList.valueOf(Color.WHITE));
                    textView8.setHintTextColor(getResources().getColor(R.color.blanco));
                    textView8.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(textView8, ColorStateList.valueOf(Color.WHITE));
                    button5.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    button6.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    button5.setTextColor(getResources().getColor(R.color.blanco));
                    button6.setTextColor(getResources().getColor(R.color.blanco));
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    anadirFoto.setTextColor(getResources().getColor(R.color.morado));
                    textView6.setTextColor(colorViejo);
                    textView9.setTextColor(colorViejo);
                    editTextTextMultiline.setHintTextColor(colorViejo);
                    editTextTextMultiline.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editTextTextMultiline, colorViejo);
                    textView8.setHintTextColor(colorViejo);
                    textView8.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(textView8, colorViejo);
                    button5.setBackgroundResource(R.drawable.botlogin);
                    button6.setBackgroundResource(R.drawable.botlogin);
                    button5.setTextColor(getResources().getColor(R.color.morado));
                    button6.setTextColor(getResources().getColor(R.color.morado));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
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
    public void cancelar(View v) {
        this.finish();
    }

}