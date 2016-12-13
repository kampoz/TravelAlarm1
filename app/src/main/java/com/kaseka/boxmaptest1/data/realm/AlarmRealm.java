package com.kaseka.boxmaptest1.data.realm;


import java.util.ArrayList;
import java.util.List;


import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class AlarmRealm extends RealmObject{

    @PrimaryKey
    private int id;
    private boolean isOn = false;
    private int alarmHour;
    private int alarmMinute;
    private int amPm;
    private String alarmDayOfWeek;
    private int alarmDayOfWeekAsInt;
    private String startPoint;
    private String destinationPoint;
    private int routeTimeInSeconds;
    private String routeTimeLabel;
    private Integer preparingTimeInMins;
    private Long alarmTimeInMillis;


    private RealmList<LatLngRealm> LngLatPointsRealmList;




    //gettery, settery
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public int getAlarmDayOfWeekAsInt() {
        return alarmDayOfWeekAsInt;
    }

    public void setAlarmDayOfWeekAsInt(int alarmDayOfWeekAsInt) {
        this.alarmDayOfWeekAsInt = alarmDayOfWeekAsInt;
    }

    public String getAlarmDayOfWeek() {
        return alarmDayOfWeek;
    }

    public void setAlarmDayOfWeek(String alarmDayOfWeek) {
        this.alarmDayOfWeek = alarmDayOfWeek;
    }

    public int getAlarmHour() {
        return alarmHour;
    }

    public void setAlarmHour(int alarmHour) {
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



    public int getAlarmMinute() {
        return alarmMinute;
    }

    public void setAlarmMinute(int alarmMinute) {
        this.alarmMinute = alarmMinute;
    }

    public Long getAlarmTimeInMillis() {
        return alarmTimeInMillis;
    }

    public void setAlarmTimeInMillis(Long alarmTimeInMillis) {
        this.alarmTimeInMillis = alarmTimeInMillis;
    }

    public int getAmPm() {
        return amPm;
    }

    public void setAmPm(int amPm) {
        this.amPm = amPm;
    }

    public boolean getIsOn() {
        return isOn;
    }

    public void setIsOn(boolean on) {
        isOn = on;
    }



    public Integer getPreparingTimeInMins() {
        return preparingTimeInMins;
    }

    public void setPreparingTimeInMins(Integer preparingTimeInMins) {
        this.preparingTimeInMins = preparingTimeInMins;
    }

    public String getRouteTimeLabel() {
        return routeTimeLabel;
    }

    public void setRouteTimeLabel(String routeTimeLabel) {
        this.routeTimeLabel = routeTimeLabel;
    }




    public int getRouteTimeInSeconds() {
        return routeTimeInSeconds;
    }

    public void setRouteTimeInSeconds(int routeTimeInSeconds) {
        this.routeTimeInSeconds = routeTimeInSeconds;
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
