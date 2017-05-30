package connect.me.activities;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import connect.me.R;
import connect.me.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {


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
    public void onLocationChanged(Location location) { //from the location object we can get the lat and lng and store it in the database for the user (his unique ID)
        if (location == null) {
            Toast.makeText(this, "Cannot get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude()); //getting the current location
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);

            //check if the marker exists
            if (myMarker != null) {
                myMarker.remove(); //removes the previous current location
            }

            //adding a marker
            MarkerOptions options = new MarkerOptions()
                    .position(ll);
            myMarker = mGoogleMap.addMarker(options);

            mGoogleMap.animateCamera(update);


        }

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
        // Here we call markers population
        testMethodPopulateWithMarkers(mGoogleMap);

    }


    //method that brings us to a specific location
    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    // region Test logic
    public class TestUser implements Parcelable {
        TestUser(String id, String name, String phone, double latitude, double longitude) {
            this.id = id;
            this.latitude = latitude;
            this.name = name;
            this.phone = phone;
            this.longitue = longitude;
        }

        public String id;
        public String name;
        public String phone;
        public double longitue;
        public double latitude;

        protected TestUser(Parcel in) {
            id = in.readString();
            name = in.readString();
            phone = in.readString();
            longitue = in.readDouble();
            latitude = in.readDouble();
        }

        public final Creator<TestUser> CREATOR = new Creator<TestUser>() {
            @Override
            public TestUser createFromParcel(Parcel in) {
                return new TestUser(in);
            }

            @Override
            public TestUser[] newArray(int size) {
                return new TestUser[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeDouble(longitue);
            dest.writeDouble(latitude);
        }
    }

    List<TestUser> listOfUsers = new ArrayList<>();

    private void testMethodPopulateWithMarkers(GoogleMap map) {
        listOfUsers.add(new TestUser("testid1", "Shawn Paul", "+23 323344", 10, 10));
        listOfUsers.add(new TestUser("testid2", "Michael Paul","+23 111111", 10, 12));
        listOfUsers.add(new TestUser("testid3", "Ivan Rambo","+23 999999", 10, 15));

        for (TestUser user : listOfUsers) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(user.latitude, user.longitue))
                    .title(user.name))
                    .setTag(user.id);
        }

        map.setOnMarkerClickListener(this);

    }

    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        String userId = (String) marker.getTag();
        for (TestUser user : listOfUsers) {
            if (userId == user.id) {
                FragmentManager fm = getSupportFragmentManager();
                // We can pass the from the selected person and retrieve him from the database
                ProfileFragment profileFragment = ProfileFragment.newInstance(user);
                profileFragment.show(fm, "fragment_profile");

                //////////////////////////////////
                Toast.makeText(this,
                        user.name + " " + user.id,
                        Toast.LENGTH_SHORT).show();
            }
        }


        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
//endregion
}
