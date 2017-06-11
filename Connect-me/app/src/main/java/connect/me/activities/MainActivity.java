package connect.me.activities;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connect.me.R;
import connect.me.databaseIntegration.models.AdditionalUserData;
import connect.me.databaseIntegration.models.User;
import connect.me.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Marker myMarker;
    List<Marker> markersList;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private DatabaseReference mDatabase;
    private List<AdditionalUserData> userData;
    private Location currentLoggedInUserLocation;
    private HashMap<String, AdditionalUserData> userIdAdditionalUserDataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("additionalUserData");
        userData = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        markersList = new ArrayList<>();
        userId = firebaseAuth.getCurrentUser().getUid();

        if (googleServicesAvailable()) {
            Toast.makeText(this, "Great!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);
            initMap();

            ProfileFragment profileFragment;
        } else {
            //no Google Maps layout
        }
    }
// region Map connection methods

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
// endregion

    //displaying new current user's location. Every time the user moves this method is called and current location is changed accordingly
    @Override
    public void onLocationChanged(Location location) { //from the location object we can get the lat and lng and store it in the database for the user (his unique ID)

        if (location == null) {
            Toast.makeText(this, "Cannot get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude()); //getting the current location
            // Getting location for use in other methods
            currentLoggedInUserLocation = location;
            mDatabase.child(userId).child("latitude").setValue(ll.latitude);
            mDatabase.child(userId).child("longitude").setValue(ll.longitude);
            //placeMarker(new LatLng(ll.latitude,ll.longitude),"");

            //CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);

            //mGoogleMap.animateCamera(update);
        }

    }

    public void placeMarker(LatLng ll, String key) {
//        //check if the marker exists
//        if (myMarker != null) {
//            myMarker.remove(); //removes the previous current location
//        }
//        Log.e("KEYS",firebaseAuth.getCurrentUser().getUid());
//        Log.e("KEY-S",key);
        if (firebaseAuth.getCurrentUser().getUid().equals(key)) {

            for (Marker m : markersList) {
//                Log.e("KEY 1",key);
//                Log.e("KEY 2",m.getTitle());
                if (m.getTitle().equals(key)) {

                    m.remove(); //removes the previous current location
                    //Log.e("REM","OVED");
                }
            }

        }

        //adding a marker
        MarkerOptions options = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(key);
        myMarker = mGoogleMap.addMarker(options);
        markersList.add(myMarker);
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
        getUsers();
        mGoogleApiClient.connect();


//        testMethodPopulateWithMarkers(googleMap);
    }


    //method that brings us to a specific location
    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    private void getUsers() {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot u : dataSnapshot.getChildren()) {

                    AdditionalUserData additionalUserData = u.getValue(AdditionalUserData.class);
                    userIdAdditionalUserDataMap.put(u.getKey(),additionalUserData);

                    //just in case
                    userData.add(additionalUserData);
                    Log.e("LOCATION", additionalUserData.getLatitude() + "-" + additionalUserData.getLongitude());
                    if (additionalUserData.getLongitude() == 0 || additionalUserData.getLatitude() == 0) {
                        return;
                    }
                    placeMarker(new LatLng(additionalUserData.getLatitude(), additionalUserData.getLongitude()), u.getKey());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                userId = firebaseAuth.getCurrentUser().getUid();
            }
        });
    }

    // region Test logic
    private void testMethodPopulateWithMarkers(GoogleMap map) {


        map.addMarker(new MarkerOptions()
                .position(new LatLng(12, 12))
                .title("gancho"))
                .setTag(1);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(12, 13))
                .title("pancho"))
                .setTag(1);
        float[] results = new float[1];
        Location.distanceBetween(12, 12, 12, 13, results);
        for (float result : results) {
            Toast.makeText(this,
                    result + "",
                    Toast.LENGTH_SHORT).show();


        }
    }






//    public boolean onMarkerClick(final Marker marker) {
//
//        // Retrieve the data from the marker.
//        String userId = (String) marker.getTag();
//        for (TestUser user : listOfUsers) {
//            if (userId == user.id) {
//                FragmentManager fm = getSupportFragmentManager();
//                // We can pass the from the selected person and retrieve him from the database
//                ProfileFragment profileFragment = ProfileFragment.newInstance(user);
//                profileFragment.show(fm, "fragment_profile");
//
//                //////////////////////////////////
//                Toast.makeText(this,
//                        user.name + " " + user.id,
//                        Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        }
//
//
//        // Return false to indicate that we have not consumed the event and that we wish
//        // for the default behavior to occur (which is for the camera to move such that the
//        // marker is centered and for the marker's info window to open, if it has one).
//        return false;
//    }
//endregion


}
