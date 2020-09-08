package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InvitationsActivity extends AppCompatActivity {

    Button meetings, feed, opciones, perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuentros);
        meetings = (Button) findViewById(R.id.attending);
        meetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),EncuentrosActivity.class);
                startActivity(intent);
            }
        });
        feed = (Button) findViewById(R.id.feed);
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Feed.class);
                startActivity(intent);
            }
        });
        /*perfil = (Button) findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),F);
                startActivity(intent);
            }
        });
        opciones = (Button) findViewById(R.id.opciones);
        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),F);
                startActivity(intent);
            }
        });
        */
    }
}