package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class crearEventoFiesta extends AppCompatActivity {
    static final int IMAGE_PICKER_REQUEST = 0;
    static final int IMAGE_PICKER_ID = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int IMAGE_CAPTURE_ID = 3;
    private static final int MAP_PICKER_REQUEST = 4;
    TextView nomevento, descrip, venue, genero, asistentes, fecha, ubicacion, anadirImagen, anadirCamara, fechaFiesta, direccion;
    EditText editNomevento, editdescrip, editvenue, editgenero, editasistentes;
    Button botonCalendar, button5, button6, mapa;
    ConstraintLayout layout;
    ImageView uploadImage;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    private int dia, mes, ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento_fiesta);
        fechaFiesta = findViewById(R.id.fechaFiesta);
        nomevento = findViewById(R.id.nomevento);
        descrip = findViewById(R.id.descrip);
        venue = findViewById(R.id.venue);
        genero = findViewById(R.id.genero);
        asistentes = findViewById(R.id.asistentes);
        fecha = findViewById(R.id.fecha);
        ubicacion = findViewById(R.id.ubicacion);
        anadirImagen = findViewById(R.id.anadirImagen);
        anadirCamara = findViewById(R.id.anadirCamara);
        editNomevento = findViewById(R.id.editNomevento);
        editdescrip = findViewById(R.id.editdescrip);
        editvenue = findViewById(R.id.editvenue);
        editgenero = findViewById(R.id.editgenero);
        editasistentes = findViewById(R.id.editasistentes);
        botonCalendar = findViewById(R.id.botonCalendar);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        layout = findViewById(R.id.layoutCrearEventoFiesta);
        uploadImage = findViewById(R.id.uploadImage);
        direccion = findViewById(R.id.direccion);
        mapa = findViewById(R.id.botonMap);

        //Sensores de luminosidad
        layout= findViewById(R.id.layoutCrearEvento);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void selectFecha(View v){
        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        ano = c.get(Calendar.YEAR);
        // DatePicker mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        //mDatePicker.updateDate(1994, 6, 12);// Sets date displayed on DatePicker to 12th June 1994
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                fechaFiesta.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
            }
        }
        , dia, mes, ano);
        datePickerDialog.show();
    }
    public void cancelar(View v){
        this.finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener,lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
    }
    public void seleccionarimagen(View v){
        solicitarPermiso(this, Manifest.permission.READ_EXTERNAL_STORAGE, "NECESITO", IMAGE_PICKER_ID);
        selecImagen();
    }
    public void abrirCamara(View v){
        solicitarPermiso(this, Manifest.permission.CAMERA, "necesito", IMAGE_CAPTURE_ID);
        tomarFoto();
    }
    public void selecImagen(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent selecImagen = new Intent(Intent.ACTION_PICK);
            selecImagen.setType("image/*");
            startActivityForResult(selecImagen, IMAGE_PICKER_REQUEST);
        }
    }

    public void selectPosition(View v){
        Intent intent = new Intent(this, eventoMapa.class);
        Bundle bundle = new Bundle();
        bundle.putInt("codigo", 1);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, MAP_PICKER_REQUEST);
    }

    public void tomarFoto(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void solicitarPermiso(Activity context, String permiso, String justificacion, int idPermiso){
        if(ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permiso)){
                Toast.makeText(this, justificacion, Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permiso}, idPermiso);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case IMAGE_PICKER_ID:
                selecImagen();
                break;
            case IMAGE_CAPTURE_ID:
                tomarFoto();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IMAGE_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    try{
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        uploadImage.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    uploadImage.setImageBitmap(imageBitmap);
                }
                break;
            case MAP_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    direccion.setText((String) extras.get("direccion"));
                }
                break;
        }
    }
}

