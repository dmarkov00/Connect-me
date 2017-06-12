package connect.me.databaseIntegration.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by Mirela on 6/1/2017.
 */

public class AdditionalUserData implements Parcelable {

    private String gender;



    private boolean filtered;
    private double latitude;
    private double longitude;
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

    protected AdditionalUserData(Parcel in) {
        gender = in.readString();
        filtered = in.readByte() != 0;
        latitude = in.readDouble();
        longitude = in.readDouble();
        phoneNumber = in.readString();
        name = in.readString();
        age = in.readInt();
    }

    public static final Creator<AdditionalUserData> CREATOR = new Creator<AdditionalUserData>() {
        @Override
        public AdditionalUserData createFromParcel(Parcel in) {
            return new AdditionalUserData(in);
        }

        @Override
        public AdditionalUserData[] newArray(int size) {
            return new AdditionalUserData[size];
        }
    };

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
    @Exclude
    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    @Override @Exclude
    public int describeContents() {
        return 0;
    }

    @Override @Exclude
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gender);
        dest.writeByte((byte) (filtered ? 1 : 0));
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(phoneNumber);
        dest.writeString(name);
        dest.writeInt(age);
    }
}

