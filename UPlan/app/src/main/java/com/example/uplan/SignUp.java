package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Arrays;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;


    TextView cont,emailview,usr;
    EditText passwordText,email,usernameText;
    Button registrarse;
    RelativeLayout layout;


    private static final String TAG = "AUTH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cont=findViewById(R.id.cont);
        email=findViewById(R.id.mailText);
        usr=findViewById(R.id.usr);
        emailview = findViewById(R.id.emailview);
        final ColorStateList colorViejo = usr.getTextColors();
        passwordText=findViewById(R.id.passText);
        usernameText=findViewById(R.id.usernameText);
        registrarse = findViewById(R.id.boton_registrarse);

        mAuth = FirebaseAuth.getInstance();



        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(email.getText().toString(), passwordText.getText().toString());
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
                    emailview.setTextColor(getResources().getColor(R.color.blanco));
                    usr.setTextColor(getResources().getColor(R.color.blanco));
                    passwordText.setHintTextColor(getResources().getColor(R.color.blanco));
                    passwordText.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(passwordText, ColorStateList.valueOf(Color.WHITE));
                    usernameText.setHintTextColor(getResources().getColor(R.color.blanco));
                    usernameText.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(usernameText, ColorStateList.valueOf(Color.WHITE));
                    email.setHintTextColor(getResources().getColor(R.color.blanco));
                    email.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(email, ColorStateList.valueOf(Color.WHITE));
                    registrarse.setBackgroundResource(R.drawable.boton_registrarse_dark);
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    cont.setTextColor(colorViejo);
                    emailview.setTextColor(colorViejo);
                    usr.setTextColor(colorViejo);
                    passwordText.setHintTextColor(colorViejo);
                    passwordText.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(passwordText, colorViejo);
                    usernameText.setHintTextColor(colorViejo);
                    usernameText.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(usernameText, colorViejo);
                    email.setHintTextColor(colorViejo);
                    email.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(email, colorViejo);
                    registrarse.setBackgroundResource(R.drawable.boton_registrarse);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }


    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){ //Update user Info
                                UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                                upcrb.setDisplayName(usernameText.getText().toString());
                                upcrb.setPhotoUri(Uri.parse("path/to/pic"));//fake uri, use Firebase Storage
                                user.updateProfile(upcrb.build());
                                updateUI(user);
                            }
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Auth failed"+ task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.e(TAG, task.getException().getMessage());
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            Intent intent = new Intent(getBaseContext(), Navigation.class);
            intent.putExtra("user", currentUser.getEmail());
            startActivity(intent);
        } else {
            email.setText("");
            passwordText.setText("");
            usernameText.setText("");
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