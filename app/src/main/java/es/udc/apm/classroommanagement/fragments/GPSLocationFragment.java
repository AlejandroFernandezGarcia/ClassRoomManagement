package es.udc.apm.classroommanagement.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import es.udc.apm.classroommanagement.MainActivity;
import es.udc.apm.classroommanagement.R;
import es.udc.apm.classroommanagement.model.Building;
import es.udc.apm.classroommanagement.services.BuildingService;

import static es.udc.apm.classroommanagement.utils.Utils.logError;
import static es.udc.apm.classroommanagement.utils.Utils.showToast;

public class GPSLocationFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener, OnMapReadyCallback {

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    private GoogleApiClient apiClient;

    private LocationRequest locRequest;
    private GoogleMap mapa;
    private Marker personMarker;
    private SupportMapFragment mapFragment;
    private BuildingService buildingService;
    private View view = null;

    public GPSLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).showLateralMenu(true);
        buildingService = new BuildingService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_gps_location, container, false);
            apiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();

            enableLocationUpdates();

            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);

        } catch (InflateException e) {
            Log.e("", e.getMessage());
        }

        return view;
    }

    private boolean isLocationServiceEnabled() {
        Context context = getActivity().getApplicationContext();
        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;

        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        return gps_enabled || network_enabled;

    }

    private void enableLocationUpdates() {
        if (isLocationServiceEnabled()) {
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
                                status.startResolutionForResult(getActivity(), PETICION_CONFIG_UBICACION);
                            } catch (IntentSender.SendIntentException e) {
                                showToast(getActivity().getApplicationContext(), "Error al intentar solucionar configuración de ubicación");
                            }

                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            showToast(getActivity().getApplicationContext(), "No se puede cumplir la configuración de ubicación necesaria");
                            break;
                    }
                }
            });
        } else {
            showToast(getActivity().getApplicationContext(), "El GPS está deshabilitado.");
        }
    }

    private void disableLocationUpdates() {
        if (isLocationServiceEnabled()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
        }
    }

    private void startLocationUpdates() {
        if (isLocationServiceEnabled()) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
                //Sería recomendable implementar la posible petición en caso de no tenerlo.


                LocationServices.FusedLocationApi.requestLocationUpdates(
                        apiClient, locRequest, GPSLocationFragment.this);
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            if (lastLocation != null) {
                moveMap(lastLocation);
                movePersonMarker(lastLocation);
            } else {
                startLocationUpdates();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        disableLocationUpdates();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        showToast(getActivity().getApplicationContext(), getString(R.string.google_services_fail));
    }


    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        showToast(getActivity().getApplicationContext(), getString(R.string.google_services_suspended));
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

                showToast(getActivity().getApplicationContext(), getString(R.string.no_gps_permission));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        showToast(getActivity().getApplicationContext(), "El usuario no ha realizado los cambios de configuración necesarios");
                        break;
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        moveMap(location);
        movePersonMarker(location);
        //remove location callback:
        disableLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mapa = map;
        mapa.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mapa.setMyLocationEnabled(true);
        if (isLocationServiceEnabled())
            map.getUiSettings().setMyLocationButtonEnabled(true);

        drawBuildingsMarkers();

    }

    private void moveMap(Location pos) {
        float zoom = mapa.getCameraPosition().zoom;
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(pos.getLatitude(), pos.getLongitude()), zoom);
        mapa.moveCamera(camUpd1);
    }

    private void movePersonMarker(Location pos) {

        LatLng latLng = new LatLng(pos.getLatitude(), pos.getLongitude());

        if (personMarker != null) {
            personMarker.setPosition(latLng);
        } else {
            personMarker = mapa.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("I am here")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_person)));
        }
    }

    private void drawBuildingsMarkers() {
        List<Building> buildings = null;
        try {
            buildings = buildingService.getAllBuildings();
        } catch (Exception e) {
            logError(this, e);
            showToast(getActivity().getApplicationContext(), getString(R.string.connection_error));
        }

        for (Building building : buildings) {
            LatLng latLng = new LatLng(building.getLatitude(), building.getLongitude());
            mapa.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(building.getName())
                    .snippet(building.getInfoString()));
        }
    }

    @Override
    public void onDestroyView() {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        Fragment fragment = mapFragment;
        if (fragment.isResumed()) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }
        apiClient.stopAutoManage(getActivity());
        apiClient.disconnect();
        mapa.clear();
        super.onDestroyView();
    }


}
