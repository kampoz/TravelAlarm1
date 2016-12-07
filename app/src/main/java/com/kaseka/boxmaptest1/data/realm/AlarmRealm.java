package com.kaseka.boxmaptest1.data.realm;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.StreamHandler;


import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class AlarmRealm extends RealmObject{

    @PrimaryKey
    private int id;
    private String alarmHour;
    private String alarmDay;
    private String startPoint;
    private String destinationPoint;
    private String dayOfWeek;
    private RealmList<LatLngRealm> LngLatPointsRealmList;




    //gettery, settery
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

    public static RealmResults<AlarmRealm> getAll() {
       return Realm.getDefaultInstance().where(AlarmRealm.class).findAll();
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    // inne metody i kwerendy
    // zamknięcie realma
    public void close() {
        Realm.getDefaultInstance().close();
    }

    //wstawienie nowego alarmu
    public void insertAlarm(final AlarmRealm alarm){
        Realm.getDefaultInstance().beginTransaction();
        AlarmRealm alarmRealm = Realm.getDefaultInstance().createObject(AlarmRealm.class);
        alarmRealm.setId(generateId());
        Realm.getDefaultInstance().commitTransaction();
    }

    // pobranie alarmu na podstawie  id
    public AlarmRealm getAlarmById(final int id) {
        AlarmRealm alarmRealm = Realm.getDefaultInstance().where(AlarmRealm.class).equalTo("id", id).findFirst();
        return alarmRealm;
    }

    // aktualizacja alarmu w bazie
    public void updateAlarm(final AlarmRealm alarm) {
        AlarmRealm alarmRealm = Realm.getDefaultInstance().where(AlarmRealm.class).equalTo("id", alarm.getId()).findFirst();
        Realm.getDefaultInstance().beginTransaction();
        //wprowadzenie zmian w alarmie: tu zmiana godziny
        alarmRealm.setAlarmHour(alarm.getAlarmHour());
        Realm.getDefaultInstance().commitTransaction();
    }


    // pobranie wszystkich alarmow
    public List<AlarmRealm> getAllAlarms() {
        List<AlarmRealm> alarms = new ArrayList<>();
        RealmResults<AlarmRealm> all = Realm.getDefaultInstance().where(AlarmRealm.class).findAll();

        for (AlarmRealm alarmRealm : all) {
            alarms.add(alarmRealm);
        }
        return alarms;
    }


    // usunięcie alarmu z bazy
    public void deleteAlarmById(final long id) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(AlarmRealm.class).equalTo("id", id).findFirst().deleteFromRealm();
            }
        });
    }

    //generowanie id
    private int generateId() {
        return Realm.getDefaultInstance().where(AlarmRealm.class).max("id").intValue() + 1;
    }

}
