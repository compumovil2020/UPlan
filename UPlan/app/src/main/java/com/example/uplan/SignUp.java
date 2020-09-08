package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button facebook = (Button) findViewById(R.id.fb);
        Button google = (Button) findViewById(R.id.google);
        Button registrarse = (Button) findViewById(R.id.boton_registrarse);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Feed.class);
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


    }
}