package com.example.uplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegisterDetails extends AppCompatActivity {

    TextView nombre, apellido, gustos, fecha, ubicacion, anadirImagen, anadirCamara, direccion;
    EditText editNombre, editApellido, editGustos;
    Button botonGustos, botonCalendar, buttonTerminar, buttonCancel, mapa;
    ConstraintLayout layout;
    LinearLayout linearArtistas, linearLinks, artistasIngresados, linksIngresados;
    ImageView uploadImage;
    Double latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        nombre = findViewById(R.id.nom);
        apellido = findViewById(R.id.ape);
        gustos = findViewById(R.id.gustos);
        fecha = findViewById(R.id.fecha);
        ubicacion = findViewById(R.id.ubicacion);



    }
}