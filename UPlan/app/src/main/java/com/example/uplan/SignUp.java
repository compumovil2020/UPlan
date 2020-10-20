package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.userText);
        password = (EditText) findViewById(R.id.passText);
        email = (EditText) findViewById(R.id.mailText);

        Button facebook = (Button) findViewById(R.id.fb);
        Button google = (Button) findViewById(R.id.google);
        Button registrarse = (Button) findViewById(R.id.boton_registrarse);
        mAuth = FirebaseAuth.getInstance();

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(email.getText().toString(), password.getText().toString());
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


    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("SIGN", "createUserWithEmail:onComplete:" + task.isSuccessful());
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){ //Update user Info
                                UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                                upcrb.setDisplayName(username.getText().toString());
                                upcrb.setPhotoUri(Uri.parse("path/to/pic"));//fake uri, use Firebase Storage
                                user.updateProfile(upcrb.build());
                                updateUI(user);
                            }
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Auth failed"+ task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.e("SIGN", task.getException().getMessage());
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
            password.setText("");
            username.setText("");
        }
    }

}