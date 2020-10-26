package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListaEventos extends AppCompatActivity {

    ListView listaDeEventos;
    TextView text;
    ConstraintLayout layout;
    List<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        listaDeEventos = findViewById(R.id.listaDeEventos);
        text = findViewById(R.id.seleccioneEvento);
        layout = findViewById(R.id.layoutSelecEvento);
        list.add("Fiesta");
        list.add("Concierto");
        list.add("Gamer");
        list.add("Deportivo");
        list.add("Evento Publico");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listaDeEventos.setAdapter(adapter);
        listaDeEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        Intent intent = new Intent(getBaseContext(), crearEventoFiesta.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getBaseContext(), crearEventoConcierto.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getBaseContext(), crearEventoGamer.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getBaseContext(), crearEventoDeportivo.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getBaseContext(), crearEventoPublico.class);
                        startActivity(intent4);
                        break;
                }
            }
        });
    }
}