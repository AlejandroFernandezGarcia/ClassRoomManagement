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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import es.udc.apm.classroommanagement.utils.PopupAdapter;

import static com.google.android.gms.location.LocationServices.*;
import static es.udc.apm.classroommanagement.utils.Utils.logError;
import static es.udc.apm.classroommanagement.utils.Utils.showToast;

public class GPSLocationFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener, OnMapReadyCallback, SwipeRefreshLayout.OnRefreshListener, GoogleMap.OnInfoWindowClickListener  {

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    private GoogleApiClient apiClient;

    private LocationRequest locRequest;
    private GoogleMap mapa;
    private Marker personMarker;
    private SupportMapFragment mapFragment;
    private static BuildingService buildingService;
    private View view = null;

    private SwipeRefreshLayout swipeLayout;


    public GPSLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).showLateralMenu(true);
        buildingService = new BuildingService();
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
                showToast(getActivity().getApplicationContext(), getString(R.string.no_gps_permission));
            }


        }
    }
    */

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

        view = inflater.inflate(R.layout.fragment_gps_location, container, false);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshGPS);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setEnabled(true);
        swipeLayout.setRefreshing(true);

        return view;
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(apiClient == null || !apiClient.isConnected())
            apiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addConnectionCallbacks(this)
                    .addApi(API)
                    .build();

        enableLocationUpdates();

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        swipeLayout.setEnabled(false);
        swipeLayout.setRefreshing(false);
    }

    private boolean isLocationServiceEnabled() {

        Context context = getActivity().getApplicationContext();
        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;

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
                    SettingsApi.checkLocationSettings(
                            apiClient, locSettingsRequest);

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                    final Status status = locationSettingsResult.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            startLocationUpdates();

                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(getActivity(), PETICION_CONFIG_UBICACION);
                            } catch (IntentSender.SendIntentException e) {
                                showToast(getActivity().getApplicationContext(), getString(R.string.location_conf_request));
                            }

                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            showToast(getActivity().getApplicationContext(), getString(R.string.location_no_settings_change));
                            break;
                    }
                }
            });
        } else {
            showToast(getActivity().getApplicationContext(), getString(R.string.location_disabled));
        }
    }

    private void disableLocationUpdates() {
        if(apiClient.isConnected()) {
            if (isLocationServiceEnabled() &&  (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                FusedLocationApi.removeLocationUpdates(apiClient, this);
            }
        }
    }

    private void startLocationUpdates() {

        if (isLocationServiceEnabled()) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                FusedLocationApi.requestLocationUpdates(
                        apiClient, locRequest, GPSLocationFragment.this);
            }
            /*
            else
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_PERMISO_LOCALIZACION);
            */
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location lastLocation =
                    FusedLocationApi.getLastLocation(apiClient);

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
        apiClient.stopAutoManage(getActivity());
        apiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        showToast(getActivity().getApplicationContext(), getString(R.string.location_settings_change));
                        break;
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //moveMap(location);
        movePersonMarker(location);
        //remove location callback:
        //disableLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mapa = map;
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

        if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(43.3329874, -8.4120048), 18.0f);
            mapa.moveCamera(camUpd1);
        }

        if (isLocationServiceEnabled() &&  (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setMyLocationButtonEnabled(true);
        }


        mapa.setInfoWindowAdapter(new PopupAdapter(LayoutInflater.from(getActivity().getApplicationContext()), getActivity().getApplicationContext()));
        mapa.setOnInfoWindowClickListener(this);
        drawBuildingsMarkers();
    }

    private void moveMap(Location pos) {
        float zoom = mapa.getCameraPosition().zoom;
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(pos.getLatitude(), pos.getLongitude()), zoom);
        mapa.moveCamera(camUpd1);
    }

    private void movePersonMarker(Location pos) {

        LatLng latLng = new LatLng(pos.getLatitude(), pos.getLongitude());
        if (isLocationServiceEnabled()) {
            if (personMarker != null) {
                personMarker.setPosition(latLng);
            } else {
                personMarker = mapa.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.popup_your_location))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_person)));

            }
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

        assert buildings != null;
        for (Building building : buildings) {
            addMarker(mapa, building.getLatitude(), building.getLongitude(), building.getName(), building.getInfoString());

        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getActivity().getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
    }

    private void addMarker(GoogleMap map, double lat, double lon,
                           String title, String snippet) {
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(title)
                .snippet(snippet));
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
