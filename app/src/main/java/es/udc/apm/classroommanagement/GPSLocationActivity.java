package es.udc.apm.classroommanagement;


import android.Manifest;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class GPSLocationActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private Button getLocationButton;
    private TextView latitudeLabel, longitudeLabel;
    private boolean gps_on = false;
    private GoogleApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_location);

        getLocationButton = (Button) findViewById(R.id.get_location_button);
        getLocationButton.setOnClickListener(this);

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
            case R.id.get_location_button:
                change_gps_status();
                break;

        }
    }

    private void change_gps_status(){

        if (gps_on){
            //gpsButton.setText(R.string.btn_gps_off);
            gps_on = false;
        }
        else{
            //gpsButton.setText(R.string.btn_gps_on);
            gps_on = true;
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


    private void showToast(String message){
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        message, Toast.LENGTH_SHORT);

        toast.show();
    }
}
