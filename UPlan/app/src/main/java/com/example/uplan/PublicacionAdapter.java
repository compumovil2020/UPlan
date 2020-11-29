package com.example.uplan;

import android.app.Activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PublicacionAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> nombre;
    private final List<String> descripcion;
    private final List<String> imgperfil;
    private final List<String> imgevento;

    private BtnClickListener mClickListener = null, mClickListener2 = null;

    private StorageReference mStorageRef;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    public PublicacionAdapter(Activity context, List<String> maintitle, List<String> subtitle, List<String> imgper, List<String> imgid, BtnClickListener listener, BtnClickListener listener2) {
        super(context, R.layout.publicacion, maintitle);

        this.context=context;
        this.nombre=maintitle;
        this.descripcion=subtitle;
        this.imgevento=imgid;
        this.imgperfil = imgper;
        this.mClickListener = listener;
        this.mClickListener2 = listener2;

        this.mStorageRef = FirebaseStorage.getInstance().getReference();


    }

    public View getView(int position,View view,ViewGroup parent) {

        final ViewHolderPublicacion holder;
        if(view==null){

            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.publicacion, parent, false);

            holder = new ViewHolderPublicacion();
            holder.layout = view.findViewById(R.id.layoutPublicacion);
            holder.nomb = (TextView) view.findViewById(R.id.tittles);
            holder.descrip = (TextView) view.findViewById(R.id.descripcion);
            holder.imgPerf = (ImageView) view.findViewById(R.id.icon);
            holder.imgEv = (ImageView) view.findViewById(R.id.imagevento);

            view.setTag(holder);

        }else{
            holder = (ViewHolderPublicacion) view.getTag();
        }

        holder.nomb.setText(nombre.get(position));
        holder.descrip.setText(descripcion.get(position));

        /*StorageReference imgRef = mStorageRef.child(imgperfil.get(position));
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context.getBaseContext())
                        .load(uri)
                        .into(holder.imgPerf);
            }
        });*/

        StorageReference imgRef2 = mStorageRef.child(imgevento.get(position));
        imgRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context.getBaseContext())
                        .load(uri)
                        .into(holder.imgEv);
            }
        });

        Button map = (Button) view.findViewById(R.id.meet);
        map.setTag(position); //For passing the list item index
        map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mClickListener != null)
                    mClickListener.onBtnClick((Integer) v.getTag());
            }
        });

        Button assist = (Button) view.findViewById(R.id.assist);
        assist.setTag(position); //For passing the list item index
        assist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mClickListener2 != null)
                    mClickListener2.onBtnClick((Integer) v.getTag());
            }
        });

        final ColorStateList colorViejo = holder.nomb.getTextColors();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    holder.layout.setBackgroundResource(R.color.dark_bg);
                    holder.nomb.setTextColor(context.getResources().getColor(R.color.blanco));
                    holder.descrip.setTextColor(context.getResources().getColor(R.color.blanco));

                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    holder.layout.setBackgroundResource(R.color.blanco);
                    holder.nomb.setTextColor(colorViejo);
                    holder.descrip.setTextColor(colorViejo);

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        return view;
    };

    static class ViewHolderPublicacion {
        RelativeLayout layout;
        TextView nomb;
        TextView descrip;
        ImageView imgPerf;
        ImageView imgEv;
    }
}

