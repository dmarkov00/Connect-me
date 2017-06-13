package connect.me.activities;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, FiltersFragment.OnFragmentInteractionListener {


    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Marker myMarker;
    List<Marker> markersList;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private DatabaseReference mDatabase;
    private List<AdditionalUserData> userData;
    private String username;
    private String email;

    //used to store all the users and maps user id to additionalUserData object
    private HashMap<String, AdditionalUserData> userIdAdditionalUserDataMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("additionalUserData");
        userData = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        markersList = new ArrayList<>();
        userId = firebaseAuth.getCurrentUser().getUid();
        username = firebaseAuth.getCurrentUser().getDisplayName();
        email = firebaseAuth.getCurrentUser().getEmail();

       if (googleServicesAvailable()) {
           setContentView(R.layout.activity_main);
            initMap();
        }

        else {
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

                        Log.v("test", position + " Begiging");
                        FragmentManager fragmentManager = getSupportFragmentManager();


                        switch (position) {

                            case 1:
                                for (Map.Entry<String, AdditionalUserData> entry : userIdAdditionalUserDataMap.entrySet()) {
                                    AdditionalUserData additionalUserData = entry.getValue();
                                    String currentUserId = entry.getKey();
                                    Log.v("test", currentUserId);
                                    Log.v("test", userId);

                                    OwnProfileFragment ownerFragment = OwnProfileFragment.newInstance(additionalUserData);
                                    ownerFragment.show(fragmentManager, "fragment_profile_own");
                                }
                                break;
                            case 2:
                                showFiltersFragment();
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
            mDatabase.child(userId).child("latitude").setValue(ll.latitude);
            mDatabase.child(userId).child("longitude").setValue(ll.longitude);
            }

    }

    public void placeMarker(LatLng ll, String key) {
        if (firebaseAuth.getCurrentUser().getUid().equals(key)) {

            for (Marker m : markersList) {
                if (m.getTitle().equals(key)) {
                    m.remove(); //removes the previous current location
                 }
            }

        }
        MarkerOptions options;

        //adding a marker
        if (userId.equals(key)) {
            options = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(key);

        } else {
            options = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(key);
        }
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
    }


    private void getUsers() {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot u : dataSnapshot.getChildren()) {


                    AdditionalUserData additionalUserData = u.getValue(AdditionalUserData.class);
                    userIdAdditionalUserDataMap.put(u.getKey(), additionalUserData);
                    Log.v("test", "iteration");

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

    private AdditionalUserData getCurrentlyLoggedInUser() {
        for (Map.Entry<String, AdditionalUserData> entry : userIdAdditionalUserDataMap.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            String currentUserId = entry.getKey();

            if (currentUserId.equals(userId)) {

                return additionalUserData;
            }
        }
        return null;
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

    private void showFiltersFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.v("test", "before");

        FiltersFragment filtersFragment = FiltersFragment.newInstance();

        filtersFragment.show(fragmentManager, "fragment_filters");
    }

    @Override
    public void onFragmentInteraction(String gender, float distance, int age) {
        Log.v("data", gender);

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
