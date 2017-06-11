package connect.me.utilities;

import android.location.Location;

/**
 * Created by markovv on 11-Jun-17.
 */

public class FilterHelpers {

    public static float getDistanceBetweenLocations(Location first, Location second) {

        return first.distanceTo(second);
    }

    public static Location convertToLocation(double longitude, double latitude) {
        Location location = new Location("");
        location.setLatitude(longitude);
        location.setLatitude(latitude);
        return location;
    }
}
