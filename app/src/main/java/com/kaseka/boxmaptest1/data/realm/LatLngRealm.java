package com.kaseka.boxmaptest1.data.realm;


import io.realm.RealmObject;

public class LatLngRealm extends RealmObject {

    double latitude;
    double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
