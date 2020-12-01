package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uplan.models.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profile extends Fragment {

    public static final String PATH_USERS="usuarios/";

    TextView nombre, bio;
    Button encuentros,feed,opciones,perfil, crEvento, edPerfil,post,myEvents;
    RelativeLayout layout;
    ImageView fotoperfil;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View profileView = inflater.inflate(R.layout.activity_profile, container, false);
        encuentros = profileView.findViewById(R.id.encuentrosU);

        feed = profileView.findViewById(R.id.feedU);
        opciones = profileView.findViewById(R.id.optionsU);
        perfil = profileView.findViewById(R.id.perfilU);
        nombre = profileView.findViewById(R.id.textView3);
        bio = profileView.findViewById(R.id.textView4);
        final ColorStateList colorViejo= nombre.getTextColors();
        crEvento = profileView.findViewById(R.id.crearEvento);
        edPerfil = profileView.findViewById(R.id.editarPerfil);
        post = profileView.findViewById(R.id.post);
        myEvents = profileView.findViewById(R.id.myEvents);
        layout=profileView.findViewById(R.id.layoutProfile);
        fotoperfil = profileView.findViewById(R.id.fotoperfil);

        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        myRef = database.getReference();

        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    nombre.setTextColor(getResources().getColor(R.color.blanco));
                    bio.setTextColor(getResources().getColor(R.color.blanco));
                    edPerfil.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    crEvento.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    edPerfil.setTextColor(getResources().getColor(R.color.blanco));
                    crEvento.setTextColor(getResources().getColor(R.color.blanco));
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    nombre.setTextColor(colorViejo);
                    bio.setTextColor(colorViejo);
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

        getPerfil();

        return profileView;

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

    public void  getPerfil(){

        myRef = database.getReference(PATH_USERS + mAuth.getCurrentUser().getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario myUser = snapshot.getValue(Usuario.class);
                String name = myUser.getNombre() + " "+ myUser.getApellido();
                nombre.setText(name);
                String bioText = "Gustos \n";
                for (String s : myUser.getGustos()){
                    bioText = bioText + " " + s + ",";
                }
                bio.setText(bioText);

                StorageReference imgRef = mStorageRef.child(myUser.getImagen());
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(Profile.this)
                                .load(uri)
                                .into(fotoperfil);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}