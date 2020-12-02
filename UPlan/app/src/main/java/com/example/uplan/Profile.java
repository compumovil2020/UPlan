package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.uplan.models.AsistirEvento;
import com.example.uplan.models.Evento;
import com.example.uplan.models.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends Fragment {

    public static final String PATH_USERS="usuarios/";

    TextView usernameperfil, fechanacimiento, gustos;
    Button encuentros,feed,opciones,perfil, crEvento, edPerfil,post,myEvents;
    ImageView imagenperfil;
    ScrollView layout;
    ListView miseventos;
    LinearLayout eventos;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    //FireBase Authentication
    private FirebaseAuth mAuth;

    //FireBase Database
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    public static final String PATH_EVENTS="eventos/";
    public static final String PATH_ASISTIR="asistirEventos/";
    public static final String PATH_USUARIOS="usuarios/";

    private final List<String> nombre = new ArrayList<>();
    private final List<String> descripcion = new ArrayList<>();
    private final List<String> imgid = new ArrayList<>();
    private final List<String> imgevento = new ArrayList<>();
    private final List<String> pubid = new ArrayList<>();
    private final List<Double> latitud = new ArrayList<>();
    private final List<Double> longitud = new ArrayList<>();

    PublicacionAdapter adapter;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View profileView = inflater.inflate(R.layout.activity_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        myRef = database.getReference(PATH_USUARIOS);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        miseventos = profileView.findViewById(R.id.listamiseventos);
        imagenperfil = profileView.findViewById(R.id.imagenperfil);
        encuentros = profileView.findViewById(R.id.encuentrosU);

        feed = profileView.findViewById(R.id.feedU);
        opciones = profileView.findViewById(R.id.optionsU);
        perfil = profileView.findViewById(R.id.perfilU);
        usernameperfil = profileView.findViewById(R.id.usernameperfil);
        fechanacimiento = profileView.findViewById(R.id.fechaNacimiento);
        gustos = profileView.findViewById(R.id.gustos);
        final ColorStateList colorViejo=fechanacimiento.getTextColors();
        crEvento = profileView.findViewById(R.id.crearEvento);
        edPerfil = profileView.findViewById(R.id.editarPerfil);
        post = profileView.findViewById(R.id.post);
        myEvents = profileView.findViewById(R.id.myEvents);
        layout=profileView.findViewById(R.id.scrollPerfil);

        readPerfil(this.getActivity());
        readMisEventos(this.getActivity());

        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    usernameperfil.setTextColor(getResources().getColor(R.color.blanco));
                    fechanacimiento.setTextColor(getResources().getColor(R.color.blanco));
                    edPerfil.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    crEvento.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    edPerfil.setTextColor(getResources().getColor(R.color.blanco));
                    crEvento.setTextColor(getResources().getColor(R.color.blanco));
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    usernameperfil.setTextColor(colorViejo);
                    fechanacimiento.setTextColor(colorViejo);
                    edPerfil.setBackgroundResource(R.drawable.botlogin);
                    crEvento.setBackgroundResource(R.drawable.botlogin);
                    edPerfil.setTextColor(getResources().getColor(R.color.morado));
                    crEvento.setTextColor(getResources().getColor(R.color.morado));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        crEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListaEventos.class);
                startActivity(intent);
            }
        });

        edPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), editarPerfil.class);
                startActivity(intent);
            }
        });

        return profileView;

    }

    public void readPerfil(final Activity activity){
        myRef = database.getReference(PATH_USUARIOS + mAuth.getCurrentUser().getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario u = snapshot.getValue(Usuario.class);
                    String name = u.getNombre() + " " + u.getApellido();
                    usernameperfil.setText(name);
                    long fecha = u.getFechaNacimiento();
                    Date date=new Date(fecha);
                    SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
                    fechanacimiento.setText("Fecha Nacimiento: " + formatDate.format(date));
                    String gustosText = "Mis gustos: \n";
                    for (String s : u.getGustos()){
                        gustosText = gustosText + s + '\n';
                    }
                    gustos.setText(gustosText);
                    StorageReference imgRef = mStorageRef.child(u.getImagen());
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(activity.getBaseContext())
                                    .load(uri)
                                    .into(imagenperfil);
                        }
                    });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readMisEventos(final Activity activity){
        myRef = database.getReference(PATH_EVENTS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                nombre.clear();
                descripcion.clear();
                imgid.clear();
                imgevento.clear();
                pubid.clear();
                for(DataSnapshot single: dataSnapshot.getChildren())
                {
                    Evento pub = single.getValue(Evento.class);
                    if(pub.getUsuarioId().equals(user.getUid())){
                        Log.i("Pruebas",single.getKey());
                        pubid.add(single.getKey());
                        nombre.add(pub.getNombrePerf());
                        descripcion.add(pub.getDescripcion());
                        imgid.add(pub.getImgperfil());
                        imgevento.add(pub.getImgevento());
                        longitud.add(pub.getLongitud());
                        latitud.add(pub.getLatitud());
                        Log.i("Pruebas", pub.getNombrePerf());
                    }
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

                BtnClickListener listener4 = new BtnClickListener() {
                    @Override
                    public void onBtnClick(int position) {
                        report(position);
                    }
                };

                adapter = new PublicacionAdapter(activity, nombre, descripcion, imgid, imgevento, listener, listener2, listener3,listener4);
                miseventos.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    public void report(int position){
        myRef = database.getReference(PATH_EVENTS+pubid.get(position));
        Map<String, Object> updates = new HashMap<>();
        updates.put("reportado", true);
        myRef.updateChildren(updates);
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