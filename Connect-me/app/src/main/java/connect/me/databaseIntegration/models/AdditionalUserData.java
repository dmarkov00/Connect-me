package connect.me.databaseIntegration.models;

import android.location.Location;

/**
 * Created by Mirela on 6/1/2017.
 */

public class AdditionalUserData {

    private String gender;
    private Location location;
    private String phoneNumber;
    private String name;
    private int age;

    public AdditionalUserData(String gender, Location location, String phoneNumber, String name, int age) {
        this.gender = gender;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
