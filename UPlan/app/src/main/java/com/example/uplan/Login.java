package com.example.uplan;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

public class Login extends AppCompatActivity {

    RelativeLayout layout;
    TextView tittle,usr,cont,registrarse,registro;
    EditText userText,passwordText;
    Button butlogin;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tittle = findViewById(R.id.tittle);
        usr=findViewById(R.id.usr);
        cont=findViewById(R.id.cont);
        final ColorStateList colorViejo = usr.getTextColors();
        registrarse=findViewById(R.id.registrarse);
        final ColorStateList colorViejoRegistrarse = registrarse.getTextColors();
        passwordText=findViewById(R.id.passwordText);
        userText=findViewById(R.id.userText);
        registro = findViewById(R.id.link_registro);
        butlogin = findViewById(R.id.boton_login);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),SignUp.class);
                startActivity(intent);
            }
        });

        butlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Navigation.class);
                startActivity(intent);
            }
        });
        layout= findViewById(R.id.layoutLogin);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    tittle.setTextColor(getResources().getColor(R.color.blanco));
                    usr.setTextColor(getResources().getColor(R.color.blanco));
                    cont.setTextColor(getResources().getColor(R.color.blanco));
                    registrarse.setTextColor(getResources().getColor(R.color.blancoPalido));
                    registro.setTextColor(getResources().getColor(R.color.accentMorado));
                    butlogin.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    butlogin.setTextColor(getResources().getColor(R.color.blanco));
                    passwordText.setHintTextColor(getResources().getColor(R.color.blanco));
                    passwordText.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(passwordText, ColorStateList.valueOf(Color.WHITE));
                    userText.setHintTextColor(getResources().getColor(R.color.blanco));
                    userText.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(userText, ColorStateList.valueOf(Color.WHITE));
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    tittle.setTextColor(getResources().getColor(R.color.negro));
                    usr.setTextColor(colorViejo);
                    cont.setTextColor(colorViejo);
                    registrarse.setTextColor(colorViejoRegistrarse);
                    registro.setTextColor(getResources().getColor(R.color.morado));
                    butlogin.setBackgroundResource(R.drawable.botlogin);
                    butlogin.setTextColor(getResources().getColor(R.color.morado));
                    passwordText.setHintTextColor(colorViejo);
                    passwordText.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(passwordText, colorViejo);
                    userText.setHintTextColor(colorViejo);
                    userText.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(userText, colorViejo);
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
}