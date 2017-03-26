package es.udc.apm.classroommanagement;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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


public class GPSLocationActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    private ToggleButton getLocationToggleButton;
    private TextView latitudeLabel, longitudeLabel;

    private GoogleApiClient apiClient;

    private LocationRequest locRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_location);

        getLocationToggleButton = (ToggleButton) findViewById(R.id.gpsToggleButton);
        getLocationToggleButton.setOnClickListener(this);

        latitudeLabel = (TextView) findViewById(R.id.latitudeLabel);
        longitudeLabel = (TextView) findViewById(R.id.longitudeLabel);

        apiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.gpsToggleButton:
                change_gps_status(getLocationToggleButton.isChecked());
                break;
        }
    }

    private void change_gps_status(boolean gps_btn_status){
        if (gps_btn_status) {
            enableLocationUpdates();
        } else {
            disableLocationUpdates();
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
                            getLocationToggleButton.setChecked(false);
                            showToast("Error al intentar solucionar configuración de ubicación");
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        showToast("No se puede cumplir la configuración de ubicación necesaria");
                        getLocationToggleButton.setChecked(false);
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


    private void updateUI(Location loc) {
        if (loc != null) {
            latitudeLabel.setText(getString(R.string.latitude_label) + ": "  + String.valueOf(loc.getLatitude()));
            longitudeLabel.setText(getString(R.string.longitude_label) + ": " + String.valueOf(loc.getLongitude()));
        } else {
            latitudeLabel.setText(getString(R.string.latitude_label) + ": (desconocida)");
            longitudeLabel.setText(getString(R.string.longitude_label) + ": (desconocida)");
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

            updateUI(lastLocation);
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

                updateUI(lastLocation);

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
                        getLocationToggleButton.setChecked(false);
                        break;
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        //Mostramos la nueva ubicación recibida
        updateUI(location);
    }

    private void showToast(String message){
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        message, Toast.LENGTH_SHORT);

        toast.show();
    }
}
