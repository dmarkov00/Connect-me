package connect.me.databaseIntegration.models;

import android.location.Location;

/**
 * Created by Mirela on 5/31/2017.
 */

public class User {
    private String email;
    private String gender;
    private Location location;
    private String phoneNumber;

    public User(String email, String gender, Location location, String phoneNumber, String name, int age) {
        this.email = email;
        this.gender = gender;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.age = age;
    }

    public User(){}

    private String name;
    private int age;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
