package com.example.uplan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
}