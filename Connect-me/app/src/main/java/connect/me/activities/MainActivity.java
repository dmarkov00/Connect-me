package connect.me.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connect.me.R;
import connect.me.databaseIntegration.models.AdditionalUserData;
import connect.me.fragments.AboutFragment;
import connect.me.fragments.FiltersFragment;
import connect.me.fragments.OwnProfileFragment;
import connect.me.fragments.ProfileFragment;

import connect.me.utilities.Filter;

import connect.me.utilities.Helpers;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, FiltersFragment.OnFragmentInteractionListener, GoogleMap.OnMarkerClickListener {


    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Marker myMarker;
    List<Marker> markersList;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private DatabaseReference mDatabase;
    private String username;
    private String email;
    private float distanceBetweenUsers;
    private Location myLocation;


    //used to store all the users and maps user id to additionalUserData object
    private HashMap<String, AdditionalUserData> userIdAdditionalUserDataMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("additionalUserData");
        firebaseAuth = FirebaseAuth.getInstance();
        markersList = new ArrayList<>();
        userId = firebaseAuth.getCurrentUser().getUid();
        username = firebaseAuth.getCurrentUser().getDisplayName();
        email = firebaseAuth.getCurrentUser().getEmail();

        if (googleServicesAvailable()) {
            setContentView(R.layout.activity_main);
            initMap();
        } else {
            //no Google Maps layout
        }

        new DrawerBuilder().withActivity(this).build();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.abc_btn_colored_material)
                .addProfiles(
                        new ProfileDrawerItem().withName(username).withEmail(email).withIcon(getResources().getDrawable(R.drawable.default_profile_picture))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("My Profile"),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Filter people"),
                        new SecondaryDrawerItem().withIdentifier(3).withName("About")

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        // Fragment manager used for displaying the different fragments
                        FragmentManager fragmentManager = getSupportFragmentManager();

                        switch (position) {

                            case 1:
                                // Retrieving the data of the logged in user
                                AdditionalUserData additionalUserData = getCurrentlyLoggedInUserAdditionalData();

                                // Displaying the fragment with the correct data
                                OwnProfileFragment ownerFragment = OwnProfileFragment.newInstance(additionalUserData);
                                ownerFragment.show(fragmentManager, "fragment_profile_own");
                                break;
                            case 2:
                                // Displaying the filter fragment
                                FiltersFragment filtersFragment = FiltersFragment.newInstance();
                                filtersFragment.show(fragmentManager, "fragment_filters");
                                break;
                            case 3:
                                // Statements
                                AboutFragment aboutFragment = AboutFragment.newInstance();
                                aboutFragment.show(fragmentManager, "fragment_about");
                                break;
                            default:
                        }
                        return true;
                    }

                })
                .build();
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
        mLocationRequest.setSmallestDisplacement(5);

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
            return;
        }
        myLocation = location;

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude()); //getting the current location
        // Getting location for use in other methods
        mDatabase.child(userId).child("latitude").setValue(ll.latitude);
        mDatabase.child(userId).child("longitude").setValue(ll.longitude);
    }

    public void placeMarker(LatLng ll, String key) {
//        if (firebaseAuth.getCurrentUser().getUid().equals(key)) {
//
//            for (Marker m : markersList) {
//                if (m.getTitle().equals(key)) {
//                    m.remove(); //removes the previous current location
//                }
//            }
//        }
        MarkerOptions options;


        //adding a marker
        if (userId.equals(key)) {
            options = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(key);

        } else {
            options = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(key);
        }


//        for (Marker m : markersList) {
//            if (m.getTitle().equals(options.getTitle())) {
//                markersList.remove(m);
//                break;
//            }
//        }


        myMarker = mGoogleMap.addMarker(options);
        markersList.add(myMarker);
        // applying filters on every added marker
//        HashMap<String, AdditionalUserData> filterResult = Filter.applyFilters(userIdAdditionalUserDataMap, myLocation, globGender, globDistance, globAge);
//        hideFilteredMarkers(filterResult);
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
        googleMap.setOnMarkerClickListener(this);
    }


    private void getUsers() {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean vibration = false;
                mGoogleMap.clear();
                for (DataSnapshot u : dataSnapshot.getChildren()) {

                    Location locationOfOthers = Helpers.convertToLocation(u.child("longitude").getValue(Double.class), u.child("latitude").getValue(Double.class));

                    if (myLocation != null && u.getKey() != userId) {
                        Log.v("theKey", u.getKey());
                        Log.v("iserId", userId);

                        distanceBetweenUsers = Helpers.getDistanceBetweenLocations(myLocation, locationOfOthers);
                    }
                    AdditionalUserData additionalUserData = null;
                    additionalUserData = u.getValue(AdditionalUserData.class);
                    userIdAdditionalUserDataMap.put(u.getKey(), additionalUserData);
                    if (distanceBetweenUsers < 10) {

                        if (u.getKey().equals(userId)) {
                            vibration = false;
                        } else {
                            vibration = true;
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(MainActivity.this)
                                            .setSmallIcon(R.drawable.notification)
                                            .setContentTitle("New notification")
                                            .setContentText(u.child("name").getValue() + " is " + distanceBetweenUsers + " meters away from you.");
                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(1, mBuilder.build());
                        }
                    }

                    if (additionalUserData != null && additionalUserData.getLongitude() != 0 && additionalUserData.getLatitude() != 0) {
                        placeMarker(new LatLng(additionalUserData.getLatitude(), additionalUserData.getLongitude()), u.getKey());
                        Log.v("trali", "ffffff");
                    }
                }
                Vibrator v = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

                if (vibration) {
                    v.vibrate(1000);
                } else {
                    v.cancel();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                userId = firebaseAuth.getCurrentUser().getUid();
            }
        });
    }

    private AdditionalUserData getCurrentlyLoggedInUserAdditionalData() {
        for (Map.Entry<String, AdditionalUserData> entry : userIdAdditionalUserDataMap.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            String currentUserId = entry.getKey();

            if (currentUserId.equals(userId)) {

                return additionalUserData;
            }
        }
        return null;
    }

//     private String globGender = "";
//    private float globDistance = 1000;
//    private int globAge = 100;
    @Override
    public void onFragmentInteraction(String gender, float distance, int age) {
// globGender = gender;
//        globDistance = distance;
//        globAge = age;
        HashMap<String, AdditionalUserData> filterResult = Filter.applyFilters(userIdAdditionalUserDataMap, myLocation, gender, distance, age);
        hideFilteredMarkers(filterResult);
    }

    private void hideFilteredMarkers(HashMap<String, AdditionalUserData> listOfFilteredUsers) {

        for (Map.Entry<String, AdditionalUserData> entry : listOfFilteredUsers.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            String userFromFilteredResult = entry.getKey();

            for (Marker m : markersList) {
                if (m.getTitle().equals(userFromFilteredResult) && additionalUserData.isFiltered() && !m.getTitle().equals(userId)) {
                    m.setVisible(false);
                }
            }
        }
    }

    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        String userIdFromMarker = (String) marker.getTitle();
        for (Map.Entry<String, AdditionalUserData> entry : userIdAdditionalUserDataMap.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            String currentUserId = entry.getKey();

            if (userIdFromMarker.equals(currentUserId)) {

                FragmentManager fm = getSupportFragmentManager();
                // We can pass the from the selected person and retrieve him from the database
                ProfileFragment profileFragment = ProfileFragment.newInstance(additionalUserData);
                profileFragment.show(fm, "fragment_profile");
                return true;

            }
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
}
