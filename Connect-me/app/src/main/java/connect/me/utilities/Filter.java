package connect.me.utilities;

import android.location.Location;
import java.util.HashMap;
import java.util.Map;
import connect.me.databaseIntegration.models.AdditionalUserData;

/**
 * Created by markovv on 11-Jun-17.
 */

public class Filter {

    public HashMap<String, AdditionalUserData> distanceFilter(Location currentlyLoggedInUser, HashMap<String, AdditionalUserData> userIdToAdditionalUserData, float distanceFilterValue) {

        for (Map.Entry<String, AdditionalUserData> entry : userIdToAdditionalUserData.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            Location peerUserLocation = FilterHelpers.convertToLocation(additionalUserData.getLongitude(), additionalUserData.getLatitude());
            float calculatedDistance = FilterHelpers.getDistanceBetweenLocations(currentlyLoggedInUser, peerUserLocation);

            if (calculatedDistance > distanceFilterValue) {
                additionalUserData.setFiltered(true);
//            } else {
//                additionalUserData.setFiltered(false);
//

            }
        }
        return userIdToAdditionalUserData;
    }

    public HashMap<String, AdditionalUserData> genderFilter(HashMap<String, AdditionalUserData> userIdToAdditionalUserData, String gender) {
        for (Map.Entry<String, AdditionalUserData> entry : userIdToAdditionalUserData.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            if (additionalUserData.getGender() == gender) {
                additionalUserData.setFiltered(true);
            }
        }
        return userIdToAdditionalUserData;
    }

    public HashMap<String, AdditionalUserData> ageFilter(HashMap<String, AdditionalUserData> userIdToAdditionalUserData, int age) {
        for (Map.Entry<String, AdditionalUserData> entry : userIdToAdditionalUserData.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            if (additionalUserData.getAge() > age) {
                additionalUserData.setFiltered(true);
            }
        }
        return userIdToAdditionalUserData;
    }

}
