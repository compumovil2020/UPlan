package com.example.uplan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class PuntoEncuentro extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] nombre;
    private final String[] evento;
    private final String[] fecha;
    private final Integer[] imgperfil;
    private final LatLng[] position;


    public PuntoEncuentro(@NonNull Context context, String[] nombre, String[] evento, String[] fecha, Integer[] imgperfil, LatLng[] position) {
        super(context, R.layout.puntoencuentro, nombre);

        this.context = (Activity) context;
        this.nombre = nombre;
        this.evento = evento;
        this.fecha = fecha;
        this.imgperfil = imgperfil;
        this.position = position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        PuntoEncuentro.ViewHolderPuntoEncuentro holder;
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

        holder.nombre.setText(nombre[position]);
        holder.imgperfil.setImageResource(imgperfil[position]);
        holder.fecha.setText(fecha[position]);
        holder.evento.setText(evento[position]);

        return view;
    }

    static class ViewHolderPuntoEncuentro {

        TextView nombre;
        TextView fecha;
        TextView evento;
        ImageView imgperfil;
    }
}
