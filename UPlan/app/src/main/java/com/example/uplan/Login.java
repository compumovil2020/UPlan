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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;

    RelativeLayout layout;
    TextView tittle,usr,cont,registrarse,registro;
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
        registro = findViewById(R.id.link_registro);
        butlogin = findViewById(R.id.boton_login);

        mAuth = FirebaseAuth.getInstance();
        TextView registro = (TextView) findViewById(R.id.link_registro);
        Button butlogin = (Button) findViewById(R.id.boton_login);

        email = (EditText) findViewById(R.id.userText);
        password = (EditText) findViewById(R.id.passwordText);

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
                signInUser(email.getText().toString(),password.getText().toString());
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
                    //registro.setTextColor(getResources().getColor(R.color.accentMorado));
                    //butlogin.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    //butlogin.setTextColor(getResources().getColor(R.color.blanco));
                    password.setHintTextColor(getResources().getColor(R.color.blanco));
                    password.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(password, ColorStateList.valueOf(Color.WHITE));
                    email.setHintTextColor(getResources().getColor(R.color.blanco));
                    email.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(email, ColorStateList.valueOf(Color.WHITE));
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    tittle.setTextColor(getResources().getColor(R.color.negro));
                    usr.setTextColor(colorViejo);
                    cont.setTextColor(colorViejo);
                    registrarse.setTextColor(colorViejoRegistrarse);
                    //registro.setTextColor(getResources().getColor(R.color.morado));
                    //butlogin.setBackgroundResource(R.drawable.botlogin);
                    //butlogin.setTextColor(getResources().getColor(R.color.morado));
                    password.setHintTextColor(colorViejo);
                    password.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(password, colorViejo);
                    email.setHintTextColor(colorViejo);
                    email.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(email, colorViejo);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            Intent intent = new Intent(getBaseContext(), Navigation.class);
            intent.putExtra("user", currentUser.getEmail());
            startActivity(intent);
        } else {
            email.setText("");
            password.setText("");
        }
    }

    private boolean validateForm() {
        boolean valid = true;
        String user = email.getText().toString();
        if (TextUtils.isEmpty(user)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }
        String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }

    private void signInUser(String email, String password) {
        if (validateForm()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("LOG", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                Log.w("LOG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
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