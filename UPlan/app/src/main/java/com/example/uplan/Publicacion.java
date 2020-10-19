package com.example.uplan;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Publicacion extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] nombre;
    private final String[] descripcion;
    private final Integer[] imgperfil;
    private final Integer[] imgevento;

    public Publicacion(Activity context, String[] maintitle,String[] subtitle, Integer[] imgid, Integer[] imgper) {
        super(context, R.layout.publicacion, maintitle);

        this.context=context;
        this.nombre=maintitle;
        this.descripcion=subtitle;
        this.imgevento=imgid;
        this.imgperfil = imgper;

    }

    public View getView(int position,View view,ViewGroup parent) {

        ViewHolderPublicacion holder;
        if(view==null){

            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.publicacion, parent, false);

            holder = new ViewHolderPublicacion();
            holder.nomb = (TextView) view.findViewById(R.id.tittles);
            holder.descrip = (TextView) view.findViewById(R.id.descripcion);
            holder.imgPerf = (ImageView) view.findViewById(R.id.icon);
            holder.imgEv = (ImageView) view.findViewById(R.id.imagevento);

            view.setTag(holder);

        }else{
            holder = (ViewHolderPublicacion) view.getTag();
        }

        holder.nomb.setText(nombre[position]);
        holder.imgPerf.setImageResource(imgperfil[position]);
        holder.descrip.setText(descripcion[position]);
        holder.imgEv.setImageResource(imgevento[position]);

        return view;

        /*LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.publicacion, parent,false);

        TextView nomb = (TextView) rowView.findViewById(R.id.tittles);
        TextView descrip = (TextView) rowView.findViewById(R.id.descripcion);
        ImageView imgPerf = (ImageView) rowView.findViewById(R.id.icon);
        ImageView imgEv = (ImageView) rowView.findViewById(R.id.imagevento);


        nomb.setText(nombre[position]);
        imgPerf.setImageResource(imgperfil[position]);
        descrip.setText(descripcion[position]);
        imgEv.setImageResource(imgevento[position]);

        return rowView;*/

    };

    static class ViewHolderPublicacion {

        TextView nomb;
        TextView descrip;
        ImageView imgPerf;
        ImageView imgEv;
    }
}

