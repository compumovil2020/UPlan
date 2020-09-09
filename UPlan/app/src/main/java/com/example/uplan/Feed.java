package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Feed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
    }

    public void Invitaciones(View v){
        Intent intent = new Intent(v.getContext(),InvitationsActivity.class);
        startActivity(intent);
    }
    public void Feed(View v){
        Intent intent = new Intent(v.getContext(),Feed.class);
        startActivity(intent);
    }
    /*public void Options(View v)
    {
        Intent intent = new Intent(v.getContext(),Options.class);
        startActivity(intent);
    }
    public void Profile(View v)
    {
        Intent intent = new Intent(v.getContext(),Profile.class);
        startActivity(intent);
    }*/
    public void Encuentros(View v)
    {
        Intent intent = new Intent(v.getContext(),EncuentrosActivity.class);
        startActivity(intent);
    }
}