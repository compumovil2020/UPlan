package com.example.uplan;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.publicacion, null,true);

        TextView nomb = (TextView) rowView.findViewById(R.id.title);
        TextView descrip = (TextView) rowView.findViewById(R.id.descripcion);
        ImageView imgPerf = (ImageView) rowView.findViewById(R.id.icon);
        ImageView imgEv = (ImageView) rowView.findViewById(R.id.imagevento);


        nomb.setText(nombre[position]);
        imgPerf.setImageResource(imgperfil[position]);
        descrip.setText(descripcion[position]);
        imgEv.setImageResource(imgevento[position]);

        return rowView;

    };
}
