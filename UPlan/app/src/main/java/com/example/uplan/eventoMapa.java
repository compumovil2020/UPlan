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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        solicitarPermiso(this, Manifest.permission.ACCESS_FINE_LOCATION, "", LOCATION_PERMISSION);
        getMyLocation();
        setSensors();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        int choice = bundle.getInt("codigo");
        switch (choice){
            case 1:
                //TODO: seleccionar una ubicacion para evento o punto de encuentro
                break;
            case 2:
                LatLng position = new LatLng(Double.parseDouble(Objects.requireNonNull(bundle.getString("latitud"))), Double.parseDouble(Objects.requireNonNull(bundle.getString("longitud"))));
                mMap.addMarker(new MarkerOptions().position(position).title(bundle.getString("evento")).snippet(geoCoderSearchLatLang(position)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

                break;
            default:
                Toast.makeText(this,"Hubo error con el Intent", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
                    //getGPS();
                } else {
                    Toast.makeText(this, "Permiso denegado para ver la localización", Toast.LENGTH_LONG).show();
                    finish();
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
                    finish();
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
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
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
                        myPositionMarker = mMap.addMarker(new MarkerOptions().position(myPosition).title("Mi posición").snippet(geoCoderSearchLatLang(myPosition)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    }
                }
            };
        }
        else{
            finish();
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