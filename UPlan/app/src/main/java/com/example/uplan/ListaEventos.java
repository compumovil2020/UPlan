package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        listaDeEventos = findViewById(R.id.listaDeEventos);
        text = findViewById(R.id.seleccioneEvento);
        final ColorStateList colorViejo = text.getTextColors();
        layout = findViewById(R.id.layoutSelecEvento);
        list.add("Fiesta");
        list.add("Concierto");
        list.add("Gamer");
        list.add("Deportivo");
        list.add("Evento Publico");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.GRAY);
                return view;
            }
        };
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
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    text.setTextColor(getResources().getColor(R.color.blanco));
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    text.setTextColor(colorViejo);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
    }
}