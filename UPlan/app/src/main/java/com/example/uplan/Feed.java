package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.uplan.models.AsistirEvento;
import com.example.uplan.models.Evento;
import com.example.uplan.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Feed extends Fragment {

    ListView list;
    RelativeLayout layout;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    private View feedView;

    //FireBase Authentication
    private FirebaseAuth mAuth;

    //FireBase Database
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static final String PATH_EVENTS="eventos/";
    public static final String PATH_ASISTIR="asistirEventos/";

    private final List<String> nombre = new ArrayList<>();
    private final List<String> descripcion = new ArrayList<>();
    private final List<String> imgid = new ArrayList<>();
    private final List<String> imgevento = new ArrayList<>();
    private final List<String> pubid = new ArrayList<>();
    private final List<Double> latitud = new ArrayList<>();
    private final List<Double> longitud = new ArrayList<>();

    String[] Latitud ={
            "4.667032","4.645036",
            "4.674460","4.602899",
            "4.670130",
    };

    String[] Longitud = {
            "-74.053035","-74.063855",
            "-74.056471","-74.060940",
            "-74.054745",

    };


    PublicacionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        feedView = inflater.inflate(R.layout.activity_feed, container, false);

        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        myRef = database.getReference(PATH_EVENTS);

        list=(ListView)feedView.findViewById(R.id.list);
        layout=feedView.findViewById(R.id.layoutFeed);

        readFeed(this.getActivity());
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    list.setBackgroundResource(R.color.dark_bg);
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    list.setBackgroundResource(R.color.blanco);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        return feedView;
    }

    private void readFeed(final Activity activity){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombre.clear();
                descripcion.clear();
                imgid.clear();
                imgevento.clear();
                pubid.clear();
                for(DataSnapshot single: dataSnapshot.getChildren())
                {
                    Evento pub = single.getValue(Evento.class);
                    Log.i("Pruebas",single.getKey());
                    pubid.add(single.getKey());
                    nombre.add(pub.getNombrePerf());
                    descripcion.add(pub.getDescripcion());
                    //imgid.add(pub.getImgperfil())
                    imgevento.add(pub.getImgevento());
                    Log.i("Pruebas", pub.getNombrePerf());
                    latitud.add(pub.getLatitud());
                    longitud.add(pub.getLongitud());
                    //if() {
                        //codigos.add(single.getKey())
                    //}


                }

                BtnClickListener listener = new BtnClickListener() {
                    @Override
                    public void onBtnClick(int position) {
                        maps(position);
                    }
                };

                BtnClickListener listener2 = new BtnClickListener() {
                    @Override
                    public void onBtnClick(int position) {
                        //Aqui se define la funcion para lo de quien asiste
                        //En position tiene el index de las listas con la info de esta publicación
                        asistirEvento(position);
                    }
                };

                BtnClickListener listener3 = new BtnClickListener() {
                    @Override
                    public void onBtnClick(int position) {
                        abrirListaAsistentes(position);
                    }
                };

                adapter = new PublicacionAdapter(activity, nombre, descripcion, imgid, imgevento, listener, listener2, listener3);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Invitaciones(View v){
        Intent intent = new Intent(v.getContext(),InvitationsActivity.class);
        startActivity(intent);
    }
    public void Feed(View v){
        Intent intent = new Intent(v.getContext(), Feed.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
    }

    public void maps(int position){
        Intent intent = new Intent(this.getContext(), eventoMapa.class);
        Bundle bundle = new Bundle();
        bundle.putInt("codigo", 2);
        bundle.putString("latitud", Double.toString(latitud.get(position)));
        bundle.putString("longitud", Double.toString(longitud.get(position)));
        //bundle.putString("evento", descripcion[position]);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }
    public void asistirEvento(final int position){
        final FirebaseUser user = mAuth.getCurrentUser();
        myRef = database.getReference(PATH_ASISTIR);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean yaAsiste = true;
                for(DataSnapshot single: snapshot.getChildren()) {
                    AsistirEvento a = single.getValue(AsistirEvento.class);
                    if (a.getIdUsuario().equals(user.getUid()) && a.getIdEvento().equals(pubid.get(position))) {
                        single.getRef().removeValue();
                        Toast.makeText(getContext(), "Ya no asistes a este evento", Toast.LENGTH_SHORT).show();
                        yaAsiste = false;
                    }
                }
                if(yaAsiste) {
                    myRef = FirebaseDatabase.getInstance().getReference("usuarios/");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot single: snapshot.getChildren()){
                                Usuario u = single.getValue(Usuario.class);

                                if(u.getId().equals(user.getUid())){
                                    AsistirEvento a = new AsistirEvento();

                                    a.setImgperfil(u.getImagen());
                                    a.setIdUsuario(user.getUid());
                                    a.setUsername(u.getUsername());
                                    a.setIdEvento(pubid.get(position));

                                    String key = myRef.push().getKey();
                                    myRef = database.getReference(PATH_ASISTIR + key);
                                    myRef.setValue(a);
                                    Toast.makeText(getContext(),"Asistencia hecha!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void abrirListaAsistentes(int position){
        Intent intent =new Intent(getContext(), ListaAsistentes.class);
        intent.putExtra("pubid", pubid.get(position));
        startActivity(intent);
    }
}