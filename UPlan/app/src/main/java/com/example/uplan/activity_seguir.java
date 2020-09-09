package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_seguir extends AppCompatActivity {
    Button seguir;
    Button perfil;
    Button opciones;
    Button feed;
    Button encuentros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguir);
        encuentros = findViewById(R.id.encuentrosU);
        feed = findViewById(R.id.feedU);
        opciones = findViewById(R.id.optionsU);
        perfil = findViewById(R.id.perfilU);
        seguir = findViewById(R.id.unfollowUsuario);

    }

    public void seguirUnfollow (View v){
      Intent intent = new Intent(this,activity_unfollow.class);
      startActivity(intent);
    }
}