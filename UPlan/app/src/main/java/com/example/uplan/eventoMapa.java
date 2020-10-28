package com.example.uplan;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import java.io.IOException;
import java.util.List;

public class eventoMapa extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION = 1;
    private static final double RADIUS_OF_EARTH_KM = 6371;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private GoogleMap mMap;
    private Geocoder mGeocoder;
    private FusedLocationProviderClient mLocationProvider;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Marker myPositionMarker, myMarker;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    private LatLng myPosition;
    private LatLng eventPosition;
    private EditText searchBar;
    private Button accion;
    public static final double lowerLeftLatitude = 1.396967;
    public static final double lowerLeftLongitude= -78.903968;
    public static final double upperRightLatitude= 11.983639;
    public static final double upperRightLongitude= -71.869905;
    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_mapa);
        accion = findViewById(R.id.accionMap);
        searchBar = findViewById(R.id.addressText);
        mLocationRequest = createLocationRequest();
        mGeocoder = new Geocoder(getBaseContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        mLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        solicitarPermiso(this, Manifest.permission.ACCESS_FINE_LOCATION, "", LOCATION_PERMISSION);
        getMyLocation();
        setSensors();
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); //tasa de refresco en milisegundos
        locationRequest.setFastestInterval(5000); //máxima tasa de refresco
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            stopLocationUpdates();
        }
    }

    private void startLocationUpdates(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mLocationProvider.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }
    private void stopLocationUpdates(){
        mLocationProvider.removeLocationUpdates(mLocationCallback);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        int choice = bundle.getInt("codigo");
        code = choice;
        switch (choice){
            case 1:
                //TODO: seleccionar una ubicacion para evento o punto de encuentro
                searchBar.setVisibility(View.VISIBLE);
                accion.setVisibility(View.VISIBLE);
                if(myPosition == null){
                    LatLng bogota = new LatLng(4.644419, -74.100483 );
                    //mMap.addMarker(new MarkerOptions().position(bogota).title("Marcador de Bogotá"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                }
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        if(myMarker != null) {
                            myMarker.remove();
                        }
                        eventPosition = latLng;
                        myMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(geoCoderSearchLatLang(latLng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        if(myPosition !=null) {
                            calcularDistancia(latLng);
                        }
                    }
                });
                readAdresses();
                accion.setText("Listo!");
                accion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(eventPosition != null) {
                            returnLocation(eventPosition);
                        }
                        else{
                            Toast.makeText(eventoMapa.this, "Debe seleccionar una ubicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 2:
                accion.setText("Ruta!");
                searchBar.setVisibility(View.INVISIBLE);
                final LatLng position = new LatLng(Double.parseDouble(Objects.requireNonNull(bundle.getString("latitud"))), Double.parseDouble(Objects.requireNonNull(bundle.getString("longitud"))));
                mMap.addMarker(new MarkerOptions().position(position).title(bundle.getString("evento") ).snippet(geoCoderSearchLatLang(position)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                accion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(myPosition != null) {
                            crearRuta(myPosition, position);
                        }
                        else{
                            Toast.makeText(eventoMapa.this,"No pudimos localizar tu posición", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            default:
                Toast.makeText(this,"Hubo error con el Intent", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private void returnLocation(LatLng destino) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("latitud", destino.latitude);
        resultIntent.putExtra("longitud", destino.longitude);
        resultIntent.putExtra("direccion", geoCoderSearchLatLang(destino));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void crearRuta(LatLng origen, LatLng destino) {
        new RutaTask(this, mMap, origen, destino).execute();
    }

    public void readAdresses(){
        searchBar = findViewById(R.id.addressText);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    String addressString = searchBar.getText().toString();
                    if (!addressString.isEmpty()) {
                        try {
                            List<Address> addresses = mGeocoder.getFromLocationName( addressString, 2, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address addressResult = addresses.get(0);
                                LatLng position = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                                if (mMap != null) {
                                    if(myMarker != null) {
                                        myMarker.remove();
                                    }
                                    eventPosition = position;
                                    myMarker = mMap.addMarker(new MarkerOptions().position(position).title(geoCoderSearchLatLang(position)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                                    if(myPosition !=null) {
                                        calcularDistancia(position);
                                    }
                                    return true;
                                }
                            } else {
                                Toast.makeText(eventoMapa.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(eventoMapa.this, "La dirección esta vacía", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }

    public void calcularDistancia(LatLng destino)
    {
        double latDistance = Math.toRadians(myPosition.latitude - destino.latitude);
        double lngDistance = Math.toRadians(myPosition.longitude - destino.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(myPosition.latitude)) * Math.cos(Math.toRadians(destino.latitude)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        Toast.makeText(this, "La distancia de aquí al punto es de: "+Math.round(result*100.0)/100.0+"km.", Toast.LENGTH_LONG).show();
    }

    public void solicitarPermiso(Activity context, String permiso, String justificacion, int idPermiso){
        if(ContextCompat.checkSelfPermission(context,permiso)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context,permiso)){
                Toast.makeText(this,justificacion,idPermiso);
            }
            ActivityCompat.requestPermissions(context,new String[]{permiso}, idPermiso);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMyLocation();
                } else {
                    Toast.makeText(this, "Permiso denegado para ver mi localización", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void getGPS()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse o) {
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {// Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult()
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(eventoMapa.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. No way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    startLocationUpdates(); //Se encendió la localización!!!
                } else {
                    Toast.makeText(this, "Sin acceso a localización, hardware deshabilitado!", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getGPS();
            mLocationProvider.getLastLocation().addOnSuccessListener(this, new
                    OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.i("LOCATION", "onSuccess location");
                            if (location != null) {
                                myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                                if(code == 1) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
                                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                                }
                            }
                        }
                    });
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        if(myPositionMarker != null) {
                            myPositionMarker.remove();
                        }
                        myPositionMarker = mMap.addMarker(new MarkerOptions().position(myPosition).title("Mi posición").snippet(geoCoderSearchLatLang(myPosition)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    }
                }
            };
        }
    }

    private String geoCoderSearchLatLang(LatLng latLng) {
        String address = "";
        try{
            List<Address> Results = mGeocoder.getFromLocation(latLng.latitude,latLng.longitude, 2);
            if(Results != null && Results.size() > 0)
            {
                address = Results.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void setSensors()
    {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (mMap != null) {
                    if (event.values[0] < 4000) {
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(eventoMapa.this,R.raw.night));
                    } else {
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(eventoMapa.this, R.raw.day));
                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };
    }
}