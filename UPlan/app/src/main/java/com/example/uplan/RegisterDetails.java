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

import com.example.uplan.models.Usuario;
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

public class RegisterDetails extends AppCompatActivity {

    public static final String PATH_USERS="usuarios/";

    static final int IMAGE_PICKER_REQUEST = 0;
    static final int IMAGE_PICKER_ID = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int IMAGE_CAPTURE_ID = 3;
    private static final int MAP_PICKER_REQUEST = 4;

    TextView nombre, apellido, gustos, fecha, ubicacion, direccion, anadirImagen, anadirCamara, fechaNacimento;
    EditText editNombre, editApellido, editGustos;
    Button botonGustos, botonCalendar, buttonTerminar, buttonCancel, mapa;
    ConstraintLayout layout;
    LinearLayout linearGustos, linearFecha, linearUbi, gustosIngresados;
    ImageView uploadImage;
    Double latitud, longitud;
    List<String> listGustos;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    private int dia, mes, ano;

    private Uri imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        myRef = database.getReference(PATH_USERS);

        nombre = findViewById(R.id.nom);
        apellido = findViewById(R.id.ape);
        gustos = findViewById(R.id.gustos);
        fecha = findViewById(R.id.fecha);
        fechaNacimento = findViewById(R.id.fechaNacimento);
        ubicacion = findViewById(R.id.ubicacion);
        direccion = findViewById(R.id.direccion);
        anadirImagen = findViewById(R.id.anadirImagen);
        anadirCamara = findViewById(R.id.anadirCamara);
        final ColorStateList colorViejo = nombre.getTextColors();

        editNombre = findViewById(R.id.editNom);
        editApellido = findViewById(R.id.editApe);
        editGustos = findViewById(R.id.editGustos);

        buttonCancel = findViewById(R.id.button6);
        buttonTerminar = findViewById(R.id.terminar);
        botonGustos = findViewById(R.id.botonGustos);
        botonCalendar = findViewById(R.id.botonCalendar);
        mapa = findViewById(R.id.botonMap);

        layout = findViewById(R.id.layoutMain);
        linearFecha = findViewById(R.id.seleccionarFecha);
        linearGustos = findViewById(R.id.linearGustos);
        linearUbi = findViewById(R.id.ubicacionContainer);
        gustosIngresados = findViewById(R.id.gustosIngresados);

        imagen = null;
        listGustos = new ArrayList<String>();

        uploadImage = findViewById(R.id.uploadImage);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < 5000) {
                    Log.i("THEME", "DARK THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.dark_bg);
                    nombre.setTextColor(getResources().getColor(R.color.blanco));
                    apellido.setTextColor(getResources().getColor(R.color.blanco));
                    gustos.setTextColor(getResources().getColor(R.color.blanco));
                    fecha.setTextColor(getResources().getColor(R.color.blanco));
                    fechaNacimento.setTextColor(getResources().getColor(R.color.blanco));
                    ubicacion.setTextColor(getResources().getColor(R.color.blanco));
                    direccion.setTextColor(getResources().getColor(R.color.blanco));
                    anadirCamara.setTextColor(getResources().getColor(R.color.accentMorado));
                    anadirImagen.setTextColor(getResources().getColor(R.color.accentMorado));
                    fecha.setTextColor(getResources().getColor(R.color.blanco));
                    editNombre.setHintTextColor(getResources().getColor(R.color.blanco));
                    editNombre.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editNombre, ColorStateList.valueOf(Color.WHITE));
                    editApellido.setHintTextColor(getResources().getColor(R.color.blanco));
                    editApellido.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editApellido, ColorStateList.valueOf(Color.WHITE));
                    editGustos.setHintTextColor(getResources().getColor(R.color.blanco));
                    editGustos.setTextColor(getResources().getColor(R.color.blanco));
                    ViewCompat.setBackgroundTintList(editGustos, ColorStateList.valueOf(Color.WHITE));
                    botonGustos.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    botonGustos.setTextColor(getResources().getColor(R.color.blanco));
                    botonCalendar.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    botonCalendar.setTextColor(getResources().getColor(R.color.blanco));
                    mapa.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    mapa.setTextColor(getResources().getColor(R.color.blanco));
                    buttonCancel.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    buttonTerminar.setBackgroundResource(R.drawable.boton_registrarse_dark);
                    buttonCancel.setTextColor(getResources().getColor(R.color.blanco));
                    buttonTerminar.setTextColor(getResources().getColor(R.color.blanco));
                } else {
                    Log.i("THEME", "LIGHT THEME " + event.values[0]);
                    layout.setBackgroundResource(R.color.blanco);
                    nombre.setTextColor(colorViejo);
                    apellido.setTextColor(colorViejo);
                    gustos.setTextColor(colorViejo);
                    fecha.setTextColor(colorViejo);
                    fechaNacimento.setTextColor(colorViejo);
                    ubicacion.setTextColor(colorViejo);
                    direccion.setTextColor(colorViejo);
                    fecha.setTextColor(colorViejo);
                    anadirCamara.setTextColor(getResources().getColor(R.color.morado));
                    anadirImagen.setTextColor(getResources().getColor(R.color.morado));
                    editNombre.setHintTextColor(colorViejo);
                    editNombre.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editNombre, colorViejo);
                    editApellido.setHintTextColor(colorViejo);
                    editApellido.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editApellido, colorViejo);
                    editGustos.setHintTextColor(colorViejo);
                    editGustos.setTextColor(colorViejo);
                    ViewCompat.setBackgroundTintList(editGustos, colorViejo);
                    mapa.setBackgroundResource(R.drawable.botlogin);
                    mapa.setTextColor(getResources().getColor(R.color.morado));
                    botonGustos.setBackgroundResource(R.drawable.botlogin);
                    botonGustos.setTextColor(getResources().getColor(R.color.morado));
                    botonCalendar.setBackgroundResource(R.drawable.botlogin);
                    botonCalendar.setTextColor(getResources().getColor(R.color.morado));
                    buttonCancel.setBackgroundResource(R.drawable.botlogin);
                    buttonTerminar.setBackgroundResource(R.drawable.botlogin);
                    buttonCancel.setTextColor(getResources().getColor(R.color.morado));
                    buttonTerminar.setTextColor(getResources().getColor(R.color.morado));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        buttonTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    register();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void register() throws ParseException {

        Usuario myUser = new Usuario();
        myUser.setNombre(editNombre.getText().toString());
        myUser.setApellido(editApellido.getText().toString());
        myUser.setEmail(mAuth.getCurrentUser().getEmail());
        myUser.setUsername(mAuth.getCurrentUser().getDisplayName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaEv = this.dia+"/"+this.mes+"/"+this.ano;
        Date date = sdf.parse(fechaEv);
        myUser.setFechaNacimiento(date.getTime());
        String img = uploadFile();
        myUser.setImagen(img);
        myUser.setLatitud(latitud);
        myUser.setLongitud(longitud);
        myUser.setGustos(listGustos);

        myRef=database.getReference(PATH_USERS+mAuth.getCurrentUser().getUid());
        myRef.setValue(myUser);

        updateUI(mAuth.getCurrentUser());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void selecFecha(View v){
        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        ano = c.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                fechaNacimento.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
            }
        }
                , dia, mes, ano);
        datePickerDialog.show();
    }

    public void agregarA(View v){
        TextView art = new TextView(this);
        art.setTextColor(getResources().getColor(R.color.design_default_color_secondary));
        art.setText(editGustos.getText());
        listGustos.add(editGustos.getText().toString());
        gustosIngresados.addView(art);
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

    private String uploadFile(){
        File f = new File(imagen.getPath());
        String imageName = f.getName();
        String path = "fotosperfil/"+mAuth.getCurrentUser().getEmail();
        StorageReference imageRef = mStorageRef.child(path);
        imageRef.putFile(imagen)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.i("Sign Up", "Succesfully upload image");
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

    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            Intent intent = new Intent(getBaseContext(), Navigation.class);
            intent.putExtra("user", currentUser.getEmail());
            startActivity(intent);
        } else {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public boolean verificar(){
        boolean valid = true;
        String text;
        text = editNombre.getText().toString();
        if (TextUtils.isEmpty(text)) {
            editNombre.setError("Required.");
            valid = false;
        } else {
            editNombre.setError(null);
        }
        text = editApellido.getText().toString();
        if (TextUtils.isEmpty(text)) {
            editApellido.setError("Required.");
            valid = false;
        } else {
            editApellido.setError(null);
        }
        text = fechaNacimento.getText().toString();
        if (TextUtils.isEmpty(text)) {
            fechaNacimento.setError("Required.");
            valid = false;
        } else {
            fechaNacimento.setError(null);
        }
        text = direccion.getText().toString();
        if (TextUtils.isEmpty(text)) {
            direccion.setError("Required.");
            valid = false;
        } else {
            direccion.setError(null);
        }
        if (this.imagen == null) {
            Toast.makeText(RegisterDetails.this, "Publish failed: Seleccione una imagen",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (this.listGustos.isEmpty()){
            editGustos.setError("Required.");
            valid = false;
        } else {
            editGustos.setError(null);
        }
        return valid;
    }
}