package es.udc.apm.classroommanagement;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import es.udc.apm.classroommanagement.daos.BuildingDao;
import es.udc.apm.classroommanagement.objects.BuildingInfo;

public class GPSLocationActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener, OnMapReadyCallback,
        View.OnClickListener{

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    private GoogleApiClient apiClient;

    private LocationRequest locRequest;
    private GoogleMap mapa;
    private Marker personMarker;

    private Button change_mode_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_location);

        apiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();

                MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        change_mode_btn = (Button)findViewById(R.id.change_mode_btn);
        change_mode_btn.setOnClickListener(this);

        enableLocationUpdates();

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.change_mode_btn:
                Intent indoorLocationIntent = new Intent(this, IndoorLocationActivity.class);
                startActivity(indoorLocationIntent);
                break;
        }
    }

    private void enableLocationUpdates() {

        locRequest = new LocationRequest();
        locRequest.setInterval(2000);
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locRequest)
                        .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        apiClient, locSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdates();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(GPSLocationActivity.this, PETICION_CONFIG_UBICACION);
                        } catch (IntentSender.SendIntentException e) {
                            showToast("Error al intentar solucionar configuración de ubicación");
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        showToast("No se puede cumplir la configuración de ubicación necesaria");
                        break;
                }
            }
        });
    }

    private void disableLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(GPSLocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Sería recomendable implementar la posible petición en caso de no tenerlo.


            LocationServices.FusedLocationApi.requestLocationUpdates(
                    apiClient, locRequest, GPSLocationActivity.this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            moveMap(lastLocation);
            movePersonMarker(lastLocation);
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        showToast(getString(R.string.google_services_fail));
    }


    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        showToast(getString(R.string.google_services_suspended));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                moveMap(lastLocation);
                movePersonMarker(lastLocation);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                showToast(getString(R.string.no_gps_permission));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        showToast("El usuario no ha realizado los cambios de configuración necesarios");
                        break;
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        moveMap(location);
        movePersonMarker(location);
    }

    private void showToast(String message){
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        message, Toast.LENGTH_SHORT);

        toast.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mapa = map;
        //mapa.getUiSettings().setZoomControlsEnabled(true);
        drawBuildingsMarkers();

    }

    private void moveMap(Location pos)
    {
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(pos.getLatitude(), pos.getLongitude()), 19);
        mapa.moveCamera(camUpd1);
    }

    private void movePersonMarker(Location pos){

        LatLng latLng = new LatLng(pos.getLatitude(), pos.getLongitude());

        if(personMarker!=null){
            personMarker.setPosition(latLng);
        }else{
            personMarker = mapa.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("I am here")
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_person)));
        }
    }

    private void drawBuildingsMarkers(){

        BuildingDao buildingDao = new BuildingDao();
        ArrayList <BuildingInfo> buildings = buildingDao.getBuildings();

        for (BuildingInfo building : buildings) {
            LatLng latLng = new LatLng(building.getLatitude(), building.getLongitude());
            mapa.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(building.getName())
                            .snippet(building.getInfoString()));
        }
    }
}

