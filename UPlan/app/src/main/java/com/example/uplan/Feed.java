package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class Feed extends AppCompatActivity {

    ListView list;

    String[] nombre ={
            "Sara Rodriguez","David Ayala",
            "Luna Diaz","Camila Ruiz",
            "Diana Gonzales",
    };

    String[] descripcion ={
            "Tarde de parche en Andino ","Evento especial en Theatron",
            "Picnic en el Virrey","Subida al cerro de Monserrate",
            "Noche de fiesta en armando",
    };

    Integer[] imgevento={
            R.drawable.person_1,R.drawable.person_2,
            R.drawable.person_3,R.drawable.person_4,
            R.drawable.person_5,
    };

    Integer[] imgid={
            R.drawable.lugar_1,R.drawable.lugar_2,
            R.drawable.lugar_3,R.drawable.lugar_4,
            R.drawable.lugar_5,
    };

    Publicacion adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        adapter = new Publicacion(this, nombre, descripcion,imgid,imgevento);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);


    }

    public void Invitaciones(View v){
        Intent intent = new Intent(v.getContext(),InvitationsActivity.class);
        startActivity(intent);
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
}