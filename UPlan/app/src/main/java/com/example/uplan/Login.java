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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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

import java.util.Arrays;

public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "AUTH_Google";

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private CallbackManager mCallbackManager;

    ConstraintLayout layout;
    TextView tittle,usr,cont,registrarse,registro;
    Button butlogin;
    LoginButton loginButton;
    SignInButton google;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.fullyInitialize();
        Log.d("Aquiiiiiiiiii", "key:" + FacebookSdk.getApplicationSignature(this));

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
        google = findViewById(R.id.google);

        email = (EditText) findViewById(R.id.userText);
        password = (EditText) findViewById(R.id.passwordText);

        googleInit();
        facebookInit();

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),SignUp.class);
                startActivity(intent);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
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

    private void googleInit() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Auth failed"+ task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void facebookInit() {

        loginButton = (LoginButton) findViewById(R.id.fb);
        loginButton.setPermissions(Arrays.asList("email", "public_profile"));
        mCallbackManager = CallbackManager.Factory.create();


        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}