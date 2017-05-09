package com.example.anna.myapplication;


import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            Toast.makeText(this, "Great!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);
            initMap();
        } else {
            //no Google Maps layout
        }

    }

    //retrieving the map fragment
    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    //check if you can connect to the Google play services
    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvalable = api.isGooglePlayServicesAvailable(this); //can be 3 values (succesfull, failure, user error (no google playsttore installed)
        if (isAvalable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvalable)) {
            Dialog dialog = api.getErrorDialog(this, isAvalable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cannot connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

//implementation of OnMapReadyCallback interface
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        //crating an object of googleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //connecting the client
        mGoogleApiClient.connect();

    }

    //method that brings us to a specific location
    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }


//implementing GoogleApiClient.ConnectionCallbacks interface, here we check if the user has enabled his location
    LocationRequest mLocationRequest;

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //gives the precise location of the user
        mLocationRequest.setInterval(1000); //every second the users location is refreshed

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               // TODO: Consider calling
               //    ActivityCompat#requestPermissions
               // here to request the missing permissions, and then overriding
               //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
               //                                          int[] grantResults)
               // to handle the case where the user grants the permission. See the documentation
               // for ActivityCompat#requestPermissions for more details.
               return;
           }
       }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this); //gets the user location depending on the mLocationRequest object

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//displaying new current user's location. Every time the user moves this method is called and current location is changed accordingly
    @Override
    public void onLocationChanged(Location location) {
        if (location == null){
            Toast.makeText(this, "Cannot get current location", Toast.LENGTH_LONG).show();
        }
        else{
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude()); //getting the current location
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,15);

            //check if the marker exists
            if (myMarker != null){
                myMarker.remove(); //removes the previous current location
            }

            //adding a marker
            MarkerOptions options = new MarkerOptions()
                                .position(ll);
            myMarker = mGoogleMap.addMarker(options);

            mGoogleMap.animateCamera(update);


        }

    }
}
