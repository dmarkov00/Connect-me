package connect.me.databaseIntegration.models;

import android.location.Location;

/**
 * Created by Mirela on 6/1/2017.
 */

public class AdditionalUserData {

    private String gender;
    double latitude;
    double longitude;
    private String phoneNumber;
    private String name;
    private int age;
    public AdditionalUserData(){}

    public AdditionalUserData(String gender, String phoneNumber, String name, int age) {
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.age = age;
        this.longitude = 0;
        this.latitude = 0;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getLatitude() {
        return latitude;
    }

//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
    public double getLongitude() {
        return longitude;
    }
//
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
