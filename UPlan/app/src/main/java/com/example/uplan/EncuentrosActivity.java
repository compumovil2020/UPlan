package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uplan.models.AsistirEvento;
import com.example.uplan.models.Evento;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EncuentrosActivity extends Fragment {

    ListView list;
    Button invitations;
    ConstraintLayout layout;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    private PuntoEncuentro adapter;

    private final List<String> events = new ArrayList<>();

    private final List<String> nombre = new ArrayList<>();
    private final List<String> descripcion = new ArrayList<>();
    private final List<Long> fecha = new ArrayList<>();
    private final List<String> img = new ArrayList<>();
    private final List<Double> latitud = new ArrayList<>();
    private final List<Double> longitud = new ArrayList<>();

    //FireBase Authentication
    private FirebaseAuth mAuth;

    //FireBase Database
    private FirebaseDatabase database;
    private DatabaseReference myRef, myRef2;
    public static final String PATH_EVENTS = "eventos/";
    public static final String PATH_ASISTIR = "asistirEventos/";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View encuentrosView = inflater.inflate(R.layout.activity_encuentros, container, false);
        layout = encuentrosView.findViewById(R.id.layoutEncuentros);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(PATH_EVENTS);

        list = (ListView) encuentrosView.findViewById(R.id.lista);

        readEvents(this.getActivity());

        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    layout.setBackgroundResource(R.color.dark_bg);
                } else {
                    layout.setBackgroundResource(R.color.blanco);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        return encuentrosView;
    }


    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
    }

    public void readEvents(final Activity activity) {

        final FirebaseUser user = mAuth.getCurrentUser();
        myRef = database.getReference(PATH_ASISTIR);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot single : snapshot.getChildren()) {
                    AsistirEvento a = single.getValue(AsistirEvento.class);
                    if (a.getIdUsuario().equals(user.getUid())) {
                        events.add(a.getIdEvento());
                    }
                }

                createLists(activity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void createLists(final Activity activity){

        myRef = database.getReference(PATH_EVENTS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot single : snapshot.getChildren()) {
                    if (events.contains(single.getKey())){
                        Evento ev = single.getValue(Evento.class);
                        nombre.add(ev.getNombreEv());
                        descripcion.add(ev.getDescripcion());
                        fecha.add(ev.getFechaEv());
                        img.add(ev.getImgevento());
                        latitud.add(ev.getLatitud());
                        longitud.add(ev.getLongitud());
                    }
                }

                BtnClickListener listener = new BtnClickListener() {
                    @Override
                    public void onBtnClick(int position) {
                        maps(position);
                    }
                };

                adapter = new PuntoEncuentro(activity, nombre, descripcion,fecha, img, latitud,longitud,listener);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void maps(int position){
        Intent intent = new Intent(this.getContext(), eventoMapa.class);
        Bundle bundle = new Bundle();
        bundle.putInt("codigo", 2);
        bundle.putString("latitud", Double.toString(latitud.get(position)));
        bundle.putString("longitud", Double.toString(longitud.get(position)));
        bundle.putString("evento", descripcion.get(position));
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

}