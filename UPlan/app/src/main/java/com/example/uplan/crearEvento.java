package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class crearEvento extends AppCompatActivity {

    Button perfil;
    Button feed;
    Button options;
    Button encuentros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);
    }

    public void feed(View v){
        Intent intent = new Intent(v.getContext(), Feed.class);
        startActivity(intent);
    }

    public void opciones(View v){
        //Intent intent = new Intent(v.getContext(), Opciones.class);
        //startActivity(intent);
    }

    public void pefil(View v){
        //Intent intent = new Intent(v.getContext(), perfil.class);
        //startActivity(intent);
    }

    public void encuentros(View v){
        Intent intent = new Intent(v.getContext(), EncuentrosActivity.class);
        startActivity(intent);
    }
}