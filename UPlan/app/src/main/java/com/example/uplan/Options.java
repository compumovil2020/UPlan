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

public class Options extends Fragment {

    Button encuentros,feed,opciones,perfil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View optionsView = inflater.inflate(R.layout.activity_options, container, false);
        encuentros = optionsView.findViewById(R.id.encuentrosU);
        feed = optionsView.findViewById(R.id.feedU);
        opciones = optionsView.findViewById(R.id.optionsU);
        perfil = optionsView.findViewById(R.id.perfilU);

        return optionsView;
    }

    public void Feed(View v){
        Intent intent = new Intent(v.getContext(), Feed.class);
        startActivity(intent);
    }

}