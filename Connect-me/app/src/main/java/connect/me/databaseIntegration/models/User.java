package connect.me.databaseIntegration.models;

import android.location.Location;

/**
 * Created by Mirela on 5/31/2017.
 */

public class User {
    private String email;
    private AdditionalUserData userData;

    public User(String email, AdditionalUserData userData) {
        this.email = email;
        this.userData =  userData;
    }

    public User(){}



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}
