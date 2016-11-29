package com.kaseka.boxmaptest1.data.realm;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AlarmRealm extends RealmObject{

    @PrimaryKey
    private int id;
    private String alarmHour;
    private String alarmDay;
    private String startPoint;
    private String destinationPoint;
    private RealmList<LatLngRealm> LngLatPointsRealmList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlarmDay() {
        return alarmDay;
    }

    public void setAlarmDay(String alarmDay) {
        this.alarmDay = alarmDay;
    }

    public String getAlarmHour() {
        return alarmHour;
    }

    public void setAlarmHour(String alarmHour) {
        this.alarmHour = alarmHour;
    }

    public String getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public RealmList getLngLatPointsRealmList() {
        return LngLatPointsRealmList;
    }

    public void setLngLatPointsRealmList(RealmList lngLatPointsRealmList) {
        LngLatPointsRealmList = lngLatPointsRealmList;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }
}
