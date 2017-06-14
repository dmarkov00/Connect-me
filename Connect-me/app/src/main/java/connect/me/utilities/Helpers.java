package connect.me.utilities;

import android.location.Location;

/**
 * Created by markovv on 11-Jun-17.
 */

public class Helpers {

    public static float getDistanceBetweenLocations(Location first, Location second) {
        if(first == null || second == null)
            return 0;

        return first.distanceTo(second);
    }

    public static Location convertToLocation(double longitude, double latitude) {
        Location location = new Location("");
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        return location;
    }
}
