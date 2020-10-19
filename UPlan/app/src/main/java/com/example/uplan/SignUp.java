package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    TextView cont,email,usr;
    EditText passwordText,emailText,userText;
    Button facebook,google,registrarse;
    RelativeLayout layout;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cont=findViewById(R.id.cont);
        email=findViewById(R.id.email);
        usr=findViewById(R.id.usr);
        final ColorStateList colorViejo = usr.getTextColors();
        passwordText=findViewById(R.id.passwordText);
        emailText=findViewById(R.id.emailText);
        userText=findViewById(R.id.userText);
        facebook = findViewById(R.id.fb);
        google = findViewById(R.id.google);
        registrarse = findViewById(R.id.boton_registrarse);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Navigation.class);
                startActivity(intent);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView web = new WebView(view.getContext());
                setContentView(web);
                web.setWebViewClient( new WebViewClient());
                web.loadUrl("https://www.facebook.com");
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView web = new WebView(view.getContext());
                setContentView(web);
                web.setWebViewClient( new WebViewClient());
                web.loadUrl("https://myaccount.google.com/");
            }
        });

        layout= findViewById(R.id.layoutSignUp);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    cont.setTextColor(getResources().getColor(R.color.blanco));
                    email.setTextColor(getResources().getColor(R.color.blanco));
                    usr.setTextColor(getResources().getColor(R.color.blanco));
                    passwordText.setHintTextColor(getResources().getColor(R.color.blanco));
                    passwordText.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(passwordText, ColorStateList.valueOf(Color.WHITE));
                    userText.setHintTextColor(getResources().getColor(R.color.blanco));
                    userText.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(userText, ColorStateList.valueOf(Color.WHITE));
                    emailText.setHintTextColor(getResources().getColor(R.color.blanco));
                    emailText.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(emailText, ColorStateList.valueOf(Color.WHITE));
                    registrarse.setBackgroundResource(R.drawable.boton_registrarse_dark);
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    cont.setTextColor(colorViejo);
                    email.setTextColor(colorViejo);
                    usr.setTextColor(colorViejo);
                    passwordText.setHintTextColor(colorViejo);
                    passwordText.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(passwordText, colorViejo);
                    userText.setHintTextColor(colorViejo);
                    userText.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(userText, colorViejo);
                    emailText.setHintTextColor(colorViejo);
                    emailText.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(emailText, colorViejo);
                    registrarse.setBackgroundResource(R.drawable.boton_registrarse);
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