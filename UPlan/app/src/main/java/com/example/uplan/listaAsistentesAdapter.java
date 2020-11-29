package com.example.uplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uplan.R;
import com.example.uplan.models.AsistirEvento;
import com.example.uplan.models.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class listaAsistentesAdapter extends ArrayAdapter<AsistirEvento> {

    private ArrayList<AsistirEvento> userArray;
    private Activity context;
    private int layoutResourceId;
    private StorageReference mStorageRef;
    //String email;

    public listaAsistentesAdapter(Activity context, ArrayList<AsistirEvento> userArray) {
        super(context, R.layout.asistentes_adapter, userArray);
        this.context = context;
        this.userArray = userArray;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.asistentes_adapter, parent, false);
        final ImageView img = view.findViewById(R.id.imagenusuario);
        TextView nombre = view.findViewById(R.id.nombreusuario);
        Log.i("adap", userArray.get(position).getUsername());
        nombre.setText(userArray.get(position).getUsername());

        StorageReference imgRef2 = mStorageRef.child(userArray.get(position).getImgperfil());
        imgRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context.getBaseContext())
                        .load(uri)
                        .into(img);
            }
        });

        return view;
    }

}
