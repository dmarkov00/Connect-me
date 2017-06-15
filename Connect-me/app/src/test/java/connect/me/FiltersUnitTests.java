package connect.me;

import android.location.Location;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import connect.me.databaseIntegration.models.AdditionalUserData;
import connect.me.utilities.Filter;

import static org.junit.Assert.assertEquals;

/**
 * Created by markovv on 15-Jun-17.
 */
@RunWith(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class)

public class FiltersUnitTests {
    HashMap<String, AdditionalUserData>  testListoFUsers;
    AdditionalUserData data = new AdditionalUserData( "gender", "phoneNumber", "name", 20);
    Location userLocation;
    private void initializeData(){
        testListoFUsers = new HashMap<String, AdditionalUserData>();
        testListoFUsers.put("key1",data);

        userLocation = new Location("");
        userLocation.setLatitude(5);
        userLocation.setLatitude(5);
    }
    @Test

    public void ageFilter_isCorrect() throws Exception {
        initializeData();
        // For all the filters we pass the dafault value except for the age
        //
        HashMap<String, AdditionalUserData> filterResult =  Filter.applyFilters(testListoFUsers,userLocation, "Male",1000,18);
        for (Map.Entry<String, AdditionalUserData> entry : filterResult.entrySet()) {
            AdditionalUserData additionalUserData = entry.getValue();

           Assert.assertEquals(additionalUserData.isFiltered(),true);
        }

    }
}
