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
import android.widget.ListView;

public class Feed extends Fragment {

    ListView list;

    String[] nombre ={
            "Sara Rodriguez","David Ayala",
            "Luna Diaz","Camila Ruiz",
            "Diana Gonzales",
    };

    String[] descripcion ={
            "Tarde de parche en Andino ","Evento especial en Theatron",
            "Picnic en el Virrey","Subida al cerro de Monserrate",
            "Noche de fiesta en armando",
    };

    Integer[] imgevento={
            R.drawable.person_1,R.drawable.person_2,
            R.drawable.person_3,R.drawable.person_4,
            R.drawable.person_5,
    };

    Integer[] imgid={
            R.drawable.lugar_1,R.drawable.lugar_2,
            R.drawable.lugar_3,R.drawable.lugar_4,
            R.drawable.lugar_5,
    };

    Publicacion adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View feedView = inflater.inflate(R.layout.activity_feed, container, false);
        adapter = new Publicacion(this.getActivity(), nombre, descripcion,imgid,imgevento);
        list=(ListView)feedView.findViewById(R.id.list);
        list.setAdapter(adapter);

        return feedView;
    }

    public void Invitaciones(View v){
        Intent intent = new Intent(v.getContext(),InvitationsActivity.class);
        startActivity(intent);
    }
    public void Feed(View v){
        Intent intent = new Intent(v.getContext(), Feed.class);
        startActivity(intent);
    }

    
}