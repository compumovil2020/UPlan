package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_unfollow extends AppCompatActivity {
    Button unfollow;
    Button perfil;
    Button opciones;
    Button feed;
    Button encuentros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfollow);
        encuentros = findViewById(R.id.encuentrosU);
        feed = findViewById(R.id.feedU);
        opciones = findViewById(R.id.optionsU);
        perfil = findViewById(R.id.perfilU);
        unfollow = findViewById(R.id.unfollowUsuario);
    }
    public void unfollowSeguir (View v){
        Intent intent = new Intent(this,activity_seguir.class);
        startActivity(intent);
    }
}