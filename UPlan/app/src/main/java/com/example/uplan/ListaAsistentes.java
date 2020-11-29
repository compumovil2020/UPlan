package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.uplan.models.AsistirEvento;
import com.example.uplan.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaAsistentes extends AppCompatActivity {
    public static final String PATH_ASISTIR="asistirEventos/";
    public static final String PATH_USUARIOS="usuarios/";

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    ListView listadeusuarios;
    listaAsistentesAdapter adbUser;
    ArrayList<Usuario> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_asistentes);
        users = new ArrayList<Usuario>();
        listadeusuarios = findViewById(R.id.listadeasistentes);
        database =FirebaseDatabase.getInstance();

        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        final String pubid =bundle.getString("pubid");

        myRef =database.getReference(PATH_ASISTIR);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<AsistirEvento> asistentes = new ArrayList<>();
                for(DataSnapshot single: snapshot.getChildren()){
                    AsistirEvento a = single.getValue(AsistirEvento.class);
                    if(a.getIdEvento().equals(pubid)){
                        asistentes.add(a);
                    }

                }
                adbUser = new listaAsistentesAdapter(ListaAsistentes.this, asistentes);
                listadeusuarios.setAdapter(adbUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}