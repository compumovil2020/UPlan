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

public class EncuentrosActivity extends Fragment {

    Button invitations, feed, opciones, perfil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View encuentrosView = inflater.inflate(R.layout.activity_encuentros, container, false);
        invitations = (Button) encuentrosView.findViewById(R.id.invitations);


        invitations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),InvitationsActivity.class);
                startActivity(intent);
            }
        });
        return encuentrosView;
    }



}