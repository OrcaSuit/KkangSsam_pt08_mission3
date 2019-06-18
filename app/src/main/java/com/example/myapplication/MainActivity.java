package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    LocationManager manager;
    Location location;
    Criteria criteria;

    GoogleMap map;
    GoogleApiClient apiClient;

    ArrayList<BankData> bankList;

    LatLng latLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String provider = manager.getBestProvider(criteria, true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = manager.getLastKnownLocation(provider);
        }

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        bankList = GeoData.getAddressData();



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.lab1_map)).getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start));
        latLng = new LatLng(location.getLatitude(),location.getLongitude());
        markerOptions.position(latLng);
        map.addMarker(markerOptions);

        CameraPosition position = new CameraPosition.Builder().target(latLng).zoom(16f).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

        MarkerOptions markerOptionsBBank = new MarkerOptions();
        markerOptionsBBank.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bank));
        for(int i=0; i<bankList.size();i++){
            LatLng bankLng = new LatLng(bankList.get(i).bankLat,bankList.get(i).bankLng);
            markerOptionsBBank.position(bankLng);
            map.addMarker(markerOptionsBBank);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiClient.connect();

    }
}
