package connect.me.utilities;

import android.location.Location;

import java.util.HashMap;
import java.util.Map;

import connect.me.databaseIntegration.models.AdditionalUserData;

/**
 * Created by markovv on 11-Jun-17.
 */

public class Filter {
    private static HashMap<String, AdditionalUserData> resetFilters(HashMap<String, AdditionalUserData> userIdToAdditionalUserData) {
        for (Map.Entry<String, AdditionalUserData> entry : userIdToAdditionalUserData.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            additionalUserData.setFiltered(false);
        }
        return userIdToAdditionalUserData;
    }

    private static HashMap<String, AdditionalUserData> distanceFilter(Location currentlyLoggedInUser, HashMap<String, AdditionalUserData> userIdToAdditionalUserData, float distanceFilterValue) {

        for (Map.Entry<String, AdditionalUserData> entry : userIdToAdditionalUserData.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            Location peerUserLocation = Helpers.convertToLocation(additionalUserData.getLongitude(), additionalUserData.getLatitude());
            float calculatedDistance = Helpers.getDistanceBetweenLocations(currentlyLoggedInUser, peerUserLocation);

            if (calculatedDistance > distanceFilterValue) {
                additionalUserData.setFiltered(true);
            }
        }
        return userIdToAdditionalUserData;
    }

    private static HashMap<String, AdditionalUserData> genderFilter(HashMap<String, AdditionalUserData> userIdToAdditionalUserData, String gender) {
        for (Map.Entry<String, AdditionalUserData> entry : userIdToAdditionalUserData.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            if (additionalUserData.getGender().equals(gender)) {
                additionalUserData.setFiltered(true);
            }
        }
        return userIdToAdditionalUserData;
    }

    private static HashMap<String, AdditionalUserData> ageFilter(HashMap<String, AdditionalUserData> userIdToAdditionalUserData, int age) {
        for (Map.Entry<String, AdditionalUserData> entry : userIdToAdditionalUserData.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();
            if (additionalUserData.getAge() > age) {
                additionalUserData.setFiltered(true);
            }
        }
        return userIdToAdditionalUserData;
    }

    // Here we can apply some optimisation and not always call all the filters, if it is the default value for example
    public static HashMap<String, AdditionalUserData> applyFilters(HashMap<String, AdditionalUserData> userIdToAdditionalUserData,
                                                                   Location currentlyLoggedInUser, String gender, float distance, int age) {
        // First we reset all the filters to be - isFiltered = false
        HashMap<String, AdditionalUserData> resetFilterResult = resetFilters(userIdToAdditionalUserData);

        // Apply distance filter
        HashMap<String, AdditionalUserData> distanceFilterResult = distanceFilter(currentlyLoggedInUser, resetFilterResult, distance);

        // Apply gender filter
        HashMap<String, AdditionalUserData> genderFilterFilterResult = genderFilter(distanceFilterResult, gender);

        // Apply age filter
        HashMap<String, AdditionalUserData> ageFilterFilterResult = ageFilter(genderFilterFilterResult, age);

        return ageFilterFilterResult;
    }

}
