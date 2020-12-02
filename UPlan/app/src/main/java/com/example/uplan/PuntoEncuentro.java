package com.example.uplan;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.uplan.models.Evento;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PuntoEncuentro extends ArrayAdapter<String> {

    private final Activity context;

    private final List<String> nombre;
    private final List<String> descripcion;
    private final List<Long> fecha;
    private final List<String> img;
    private final List<Double> latitud;
    private final List<Double> longitud;

    private BtnClickListener mClickListener = null;

    //FireBase Database
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static final String PATH_EVENTS="eventos/";
    private StorageReference mStorageRef;


    public PuntoEncuentro(@NonNull Context context, List<String>  nombre, List<String>  descripcion, List<Long>  fecha, List<String>  img, List<Double> latitud,List<Double> longitud, BtnClickListener listener) {
        super(context, R.layout.puntoencuentro, nombre);

        this.context = (Activity) context;

        this.mStorageRef = FirebaseStorage.getInstance().getReference();
        this.database= FirebaseDatabase.getInstance();

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.img = img;
        this.latitud = latitud;
        this.longitud = longitud;
        this.mClickListener = listener;
    }



    public View getView(int position, View view, ViewGroup parent) {
        final PuntoEncuentro.ViewHolderPuntoEncuentro holder;
        if(view == null){

            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.puntoencuentro, parent, false);

            holder = new PuntoEncuentro.ViewHolderPuntoEncuentro();
            holder.nombre = (TextView) view.findViewById(R.id.NombreEncuentro);
            holder.fecha = (TextView) view.findViewById(R.id.FechaEncuentro);
            holder.evento = (TextView) view.findViewById(R.id.EventoEncuentro);
            holder.imgperfil = (ImageView) view.findViewById(R.id.imageView3);

            view.setTag(holder);

        }else{
            holder = (PuntoEncuentro.ViewHolderPuntoEncuentro) view.getTag();
        }

        holder.nombre.setText(this.nombre.get(position));
        holder.evento.setText(this.descripcion.get(position));
        Date date=new Date(this.fecha.get(position));
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        holder.fecha.setText(formatDate.format(date));
        StorageReference imgRef = mStorageRef.child(this.img.get(position));
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context.getBaseContext())
                        .load(uri)
                        .into(holder.imgperfil);
            }
        });

        Button map = (Button) view.findViewById(R.id.ir_con_mapa);
        map.setTag(position); //For passing the list item index
        map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mClickListener != null)
                    mClickListener.onBtnClick((Integer) v.getTag());
            }
        });

        return view;
    }

    static class ViewHolderPuntoEncuentro {

        TextView nombre;
        TextView fecha;
        TextView evento;
        ImageView imgperfil;
    }
}
