package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Profile extends Fragment {



    Button encuentros,feed,opciones,perfil, crEvento, edPerfil;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.activity_profile, container, false);
        encuentros = profileView.findViewById(R.id.encuentrosU);
        feed = profileView.findViewById(R.id.feedU);
        opciones = profileView.findViewById(R.id.optionsU);
        perfil = profileView.findViewById(R.id.perfilU);
        crEvento = profileView.findViewById(R.id.crearEvento);
        edPerfil = profileView.findViewById(R.id.editarPerfil);

        crEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), crearEvento.class);
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


    public void Feed(View v){
        Intent intent = new Intent(v.getContext(), Feed.class);
        startActivity(intent);
    }
    
}