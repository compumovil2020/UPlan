package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Profile extends AppCompatActivity {

    Button encuentros,feed,opciones,perfil, crEvento, edPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        encuentros = findViewById(R.id.encuentrosU);
        feed = findViewById(R.id.feedU);
        opciones = findViewById(R.id.optionsU);
        perfil = findViewById(R.id.perfilU);
        crEvento = findViewById(R.id.crearEvento);
        edPerfil = findViewById(R.id.editarPerfil);
    }
    public void Feed(View v){
        Intent intent = new Intent(v.getContext(), Feed.class);
        startActivity(intent);
    }
    public void Options(View v)
    {
        Intent intent = new Intent(v.getContext(),Options.class);
        startActivity(intent);
    }
    public void Profile(View v)
    {
        Intent intent = new Intent(v.getContext(),Profile.class);
        startActivity(intent);
    }
    public void Encuentros(View v)
    {
        Intent intent = new Intent(v.getContext(),EncuentrosActivity.class);
        startActivity(intent);
    }
    public void editarPerfil(View v)
    {
        Intent intent = new Intent(v.getContext(), editarPerfil.class);
        startActivity(intent);
    }
    public void crearEvento(View v)
    {
        Intent intent = new Intent(v.getContext(),crearEvento.class);
        startActivity(intent);
    }
}