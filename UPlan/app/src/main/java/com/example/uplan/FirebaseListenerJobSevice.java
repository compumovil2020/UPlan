package com.example.uplan;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.renderscript.Sampler;
import android.util.Log;

import com.example.uplan.Feed;
import com.example.uplan.R;
import com.example.uplan.crearEvento;
import com.example.uplan.models.Evento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class FirebaseListenerJobSevice extends JobIntentService {
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    public static final String PATH_USERS="eventos/";
    Map<String, String> eventos;
    String email;

    private static final int JOB_ID = 13;

    public static void enqueueWork(Context context, Intent intent){
        enqueueWork(context, FirebaseListenerJobSevice.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = firebaseDatabase.getReference(PATH_USERS);
        eventos = new HashMap<>();
        Log.i("entro", "entroooooooooooooooo");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    Evento u = singleSnapshot.getValue(Evento.class);
                    eventos.put(u.getNombreEv(), u.getUsuarioId());
                    Log.i("user", u.getUsuarioId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Evento> cambios = new ArrayList<>();
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    Evento u = singleSnapshot.getValue(Evento.class);
                    cambios.add(u);
                }
                for(int i = 0; i < cambios.size(); i++){
                    if(cambios.get(i).getNombreEv() != eventos.get(cambios.get(i).getNombreEv()) && cambios.get(i).getNombreEv() != eventos.get(cambios.get(i).getNombreEv())){
                        Log.i("user", cambios.get(i).getUsuarioId());
                        email = cambios.get(i).getNombreEv();
                        buildAndShowNotification("Nuevo evento te espera", cambios.get(i).getNombreEv() + " esta disponible el "+cambios.get(i).getNombreEv());

                        eventos.put(cambios.get(i).getNombreEv(), cambios.get(i).getUsuarioId());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void buildAndShowNotification(String title, String message){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, crearEvento.CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.logo2);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Log.i("user", "le mando este email: " + email);
        Intent inten = new Intent(this, Feed.class);
        inten.putExtra("email2", email);
        inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, inten, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        int notificationId = 001;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, mBuilder.build());
    }
}

