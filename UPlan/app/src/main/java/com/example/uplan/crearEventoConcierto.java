package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uplan.models.EventoConcierto;
import com.example.uplan.models.EventoFiesta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class crearEventoConcierto extends AppCompatActivity {

    public static final String PATH_EVENTS="eventos/";

    static final int IMAGE_PICKER_REQUEST = 0;
    static final int IMAGE_PICKER_ID = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int IMAGE_CAPTURE_ID = 3;
    private static final int MAP_PICKER_REQUEST = 4;

    public static final String CHANNEL_ID = "MY_NOTIF_CHANNEL";

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;

    TextView nomevento, descrip, venue, artistas, links, asistentes, fecha, ubicacion, anadirImagen, anadirCamara, fechaConcierto, direccion;
    EditText editNomevento, editdescrip, editvenue, editartistas, editlinks, editasistentes;
    Button botonArtista, botonLinks, botonCalendar, publicarConcierto, button6, mapa;
    ConstraintLayout layout;
    LinearLayout linearArtistas, linearLinks, artistasIngresados, linksIngresados;
    ImageView uploadImage;
    Double latitud, longitud;
    List<String> listArtistas, listLinks;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    ArrayList<String> listartistas;
    ArrayList<String> listalinks;
    private Uri filePath;


    private int dia, mes, ano;

    private Uri imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento_concierto);
        createNotificationChannel();

        fechaConcierto = findViewById(R.id.fechaConcierto);
        nomevento = findViewById(R.id.nomevento);
        final ColorStateList colorViejo = nomevento.getTextColors();
        descrip = findViewById(R.id.descrip);
        venue = findViewById(R.id.venue);
        artistas = findViewById(R.id.artistas);
        links = findViewById(R.id.links);
        asistentes = findViewById(R.id.asistentes);
        fecha = findViewById(R.id.fecha);
        ubicacion = findViewById(R.id.ubicacion);
        anadirImagen = findViewById(R.id.anadirImagen);
        anadirCamara = findViewById(R.id.anadirCamara);
        editNomevento = findViewById(R.id.editNomevento);
        editdescrip = findViewById(R.id.editdescrip);
        editvenue = findViewById(R.id.editvenue);
        editartistas = findViewById(R.id.editartistas);
        editlinks = findViewById(R.id.editlinks);
        editasistentes = findViewById(R.id.editasistentes);
        botonArtista = findViewById(R.id.botonArtista);
        botonLinks = findViewById(R.id.botonLinks);
        botonCalendar = findViewById(R.id.botonCalendar);
        publicarConcierto = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        linearArtistas = findViewById(R.id.linearArtistas);
        linearLinks = findViewById(R.id.linearLinks);
        artistasIngresados = findViewById(R.id.artistasIngresados);
        linksIngresados = findViewById(R.id.linksIngresados);
        layout = findViewById(R.id.layoutCrearEventoConcierto);
        uploadImage = findViewById(R.id.uploadImage);
        direccion = findViewById(R.id.direccion);
        mapa = findViewById(R.id.botonMap);
        imagen = null;

        listArtistas = new ArrayList<String>();
        listLinks = new ArrayList<String>();

        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        myRef = database.getReference(PATH_EVENTS);

        //Sensores de luminosidad
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    nomevento.setTextColor(getResources().getColor(R.color.blanco));
                    descrip.setTextColor(getResources().getColor(R.color.blanco));
                    venue.setTextColor(getResources().getColor(R.color.blanco));
                    artistas.setTextColor(getResources().getColor(R.color.blanco));
                    links.setTextColor(getResources().getColor(R.color.blanco));
                    asistentes.setTextColor(getResources().getColor(R.color.blanco));
                    fecha.setTextColor(getResources().getColor(R.color.blanco));
                    ubicacion.setTextColor(getResources().getColor(R.color.blanco));
                    direccion.setTextColor(getResources().getColor(R.color.blanco));
                    anadirCamara.setTextColor(getResources().getColor(R.color.accentMorado));
                    anadirImagen.setTextColor(getResources().getColor(R.color.accentMorado));
                    fechaConcierto.setTextColor(getResources().getColor(R.color.blanco));
                    editNomevento.setHintTextColor(getResources().getColor(R.color.blanco));
                    editNomevento.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editNomevento, ColorStateList.valueOf(Color.WHITE));
                    editdescrip.setHintTextColor(getResources().getColor(R.color.blanco));
                    editdescrip.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editdescrip, ColorStateList.valueOf(Color.WHITE));
                    editvenue.setHintTextColor(getResources().getColor(R.color.blanco));
                    editvenue.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editvenue, ColorStateList.valueOf(Color.WHITE));
                    editartistas.setHintTextColor(getResources().getColor(R.color.blanco));
                    editartistas.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editartistas, ColorStateList.valueOf(Color.WHITE));
                    editlinks.setHintTextColor(getResources().getColor(R.color.blanco));
                    editlinks.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editlinks, ColorStateList.valueOf(Color.WHITE));
                    editasistentes.setHintTextColor(getResources().getColor(R.color.blanco));
                    editasistentes.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editasistentes, ColorStateList.valueOf(Color.WHITE));
                    botonArtista.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    botonArtista.setTextColor(getResources().getColor(R.color.blanco));
                    botonLinks.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    botonLinks.setTextColor(getResources().getColor(R.color.blanco));
                    botonCalendar.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    botonCalendar.setTextColor(getResources().getColor(R.color.blanco));
                    mapa.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    mapa.setTextColor(getResources().getColor(R.color.blanco));
                    publicarConcierto.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    button6.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    publicarConcierto.setTextColor(getResources().getColor(R.color.blanco));
                    button6.setTextColor(getResources().getColor(R.color.blanco));
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    nomevento.setTextColor(colorViejo);
                    descrip.setTextColor(colorViejo);
                    venue.setTextColor(colorViejo);
                    artistas.setTextColor(colorViejo);
                    links.setTextColor(colorViejo);
                    asistentes.setTextColor(colorViejo);
                    fecha.setTextColor(colorViejo);
                    ubicacion.setTextColor(colorViejo);
                    fechaConcierto.setTextColor(colorViejo);
                    direccion.setTextColor(colorViejo);
                    anadirCamara.setTextColor(getResources().getColor(R.color.morado));
                    anadirImagen.setTextColor(getResources().getColor(R.color.morado));
                    editNomevento.setHintTextColor(colorViejo);
                    editNomevento.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editNomevento, colorViejo);
                    editdescrip.setHintTextColor(colorViejo);
                    editdescrip.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editdescrip, colorViejo);
                    editvenue.setHintTextColor(colorViejo);
                    editvenue.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editvenue, colorViejo);
                    editartistas.setHintTextColor(colorViejo);
                    editartistas.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editartistas, colorViejo);
                    editlinks.setHintTextColor(colorViejo);
                    editlinks.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editlinks, colorViejo);
                    editasistentes.setHintTextColor(colorViejo);
                    editasistentes.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editasistentes, colorViejo);
                    mapa.setBackgroundResource(R.drawable.botlogin);
                    mapa.setTextColor(getResources().getColor(R.color.morado));
                    botonArtista.setBackgroundResource(R.drawable.botlogin);
                    botonArtista.setTextColor(getResources().getColor(R.color.morado));
                    botonLinks.setBackgroundResource(R.drawable.botlogin);
                    botonLinks.setTextColor(getResources().getColor(R.color.morado));
                    botonCalendar.setBackgroundResource(R.drawable.botlogin);
                    botonCalendar.setTextColor(getResources().getColor(R.color.morado));
                    publicarConcierto.setBackgroundResource(R.drawable.botlogin);
                    button6.setBackgroundResource(R.drawable.botlogin);
                    publicarConcierto.setTextColor(getResources().getColor(R.color.morado));
                    button6.setTextColor(getResources().getColor(R.color.morado));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        publicarConcierto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    publicar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void selecFecha(View v){
        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        ano = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                fechaConcierto.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
            }
        }
                , ano, mes, dia);
        datePickerDialog.show();
    }



    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            Intent intent = new Intent(getBaseContext(), Navigation.class);
            startActivity(intent);
        }
    }

    public void agregarA(View v){
        TextView art = new TextView(this);
        art.setTextColor(getResources().getColor(R.color.design_default_color_secondary));
        art.setText(editartistas.getText());
        listArtistas.add(editartistas.getText().toString());
        artistasIngresados.addView(art);
    }
    public void agregarL(View v){
        TextView link = new TextView(this);
        link.setTextColor(getResources().getColor(R.color.design_default_color_secondary));
        link.setText(editlinks.getText());
        listLinks.add(editlinks.getText().toString());
        linksIngresados.addView(link);
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
                        this.imagen = imageUri;
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
                    this.imagen = data.getData();
                    Bundle extras = data.getExtras();
                    filePath = data.getData();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    uploadImage.setImageBitmap(imageBitmap);
                }
                break;
            case MAP_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    direccion.setText((String) extras.get("direccion"));
                    this.latitud = (Double) extras.get("latitud");
                    this.longitud = (Double) extras.get("longitud");
                }
                break;
        }
    }

    public void publicar() throws ParseException {
        if(verificar()){
            EventoConcierto evento = new EventoConcierto();
            FirebaseUser user = mAuth.getCurrentUser();
            evento.setUsuarioId(user.getUid());
            evento.setTipo("Concierto");
            evento.setDescripcion(editdescrip.getText().toString());
            evento.setNombreEv(editNomevento.getText().toString());
            evento.setVenue(editvenue.getText().toString());
            evento.setAsistentes(Integer.parseInt(editasistentes.getText().toString()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaEv = this.dia+"/"+this.mes+"/"+this.ano;
            Date date = sdf.parse(fechaEv);
            evento.setFechaEv(date.getTime());
            evento.setFechaPub(System.currentTimeMillis());
            String ruta = uploadFile();
            evento.setImgevento(ruta);
            evento.setNombrePerf(mAuth.getCurrentUser().getDisplayName());
            evento.setImgperfil("fotosperfil/"+mAuth.getCurrentUser().getEmail());
            evento.setLatitud(this.latitud);
            evento.setLongitud(this.longitud);
            evento.setArtistas(this.listArtistas);
            evento.setLinks(this.listLinks);

            String key = myRef.push().getKey();
            myRef=database.getReference(PATH_EVENTS+key);
            myRef.setValue(evento);


            Intent intent = new Intent(crearEventoConcierto.this, Navigation.class);
            startActivity(intent);
        }
    }

    private String uploadFile(){
        File f = new File(imagen.getPath());
        String imageName = f.getName();
        String path = "images/Conciertos/"+ imageName;
        StorageReference imageRef = mStorageRef.child(path);
        imageRef.putFile(imagen)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.i("EventoFiesta", "Succesfully upload image");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
        return path;
    }

    public boolean verificar(){
        boolean valid = true;
        String text;
        text = editNomevento.getText().toString();
        if (TextUtils.isEmpty(text)) {
            editNomevento.setError("Required.");
            valid = false;
        } else {
            editNomevento.setError(null);
        }
        text = editdescrip.getText().toString();
        if (TextUtils.isEmpty(text)) {
            editdescrip.setError("Required.");
            valid = false;
        } else {
            editdescrip.setError(null);
        }
        text = editvenue.getText().toString();
        if (TextUtils.isEmpty(text)) {
            editvenue.setError("Required.");
            valid = false;
        } else {
            editvenue.setError(null);
        }
        text = editasistentes.getText().toString();
        if (TextUtils.isEmpty(text)) {
            editasistentes.setError("Required.");
            valid = false;
        } else {
            editasistentes.setError(null);
        }
        text = fechaConcierto.getText().toString();
        if (TextUtils.isEmpty(text)) {
            fechaConcierto.setError("Required.");
            valid = false;
        } else {
            fechaConcierto.setError(null);
        }
        text = direccion.getText().toString();
        if (TextUtils.isEmpty(text)) {
            direccion.setError("Required.");
            valid = false;
        } else {
            direccion.setError(null);
        }
        if (this.imagen == null) {
            Toast.makeText(crearEventoConcierto.this, "Publish failed: Seleccione una imagen",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (this.listLinks.isEmpty()){
            editlinks.setError("Required.");
            valid = false;
        } else {
            editlinks.setError(null);
        }
        if (this.listArtistas.isEmpty()){
            editartistas.setError("Required.");
            valid = false;
        } else {
            editartistas.setError(null);
        }
        return valid;

    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "channel";
            String description = "channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}