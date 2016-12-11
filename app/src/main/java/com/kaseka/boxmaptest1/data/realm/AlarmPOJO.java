package com.kaseka.boxmaptest1.data.realm;


import io.realm.Realm;
import io.realm.RealmList;

public class AlarmPOJO {

    private static int id;
    public static boolean isOn = false;
    public static String alarmHour;
    public static String alarmMinute;
    public static String alarmDay;
    public static String startPoint;
    public static String destinationPoint;
    public static String dayOfWeek;
    public static String routeTime;
    public static Integer preparingTimeInMins;
    public static Long wakeUpTimeInMillis;
    public static int amPm;
    public static RealmList<LatLngRealm> LngLatPointsRealmList = new RealmList<>();

    //  KONSTRUKTOR I KLASY sINGLETONA
//    private AlarmPOJO(){}
//
//    private final static class SingletonHolder {
//        private final static AlarmPOJO instance = new AlarmPOJO();
//    }
//
//    public final static AlarmPOJO getInstance() {
//        return SingletonHolder.instance;
//    }

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

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public int getAlarmId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RealmList<LatLngRealm> getLngLatPointsRealmList() {
        return LngLatPointsRealmList;
    }

    public void setLngLatPointsRealmList(RealmList<LatLngRealm> lngLatPointsRealmList) {
        LngLatPointsRealmList = lngLatPointsRealmList;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }



    //wstawianie nowego alarmu
    public void insertAlarm(final AlarmRealm alarm){
        Realm.getDefaultInstance().beginTransaction();
        AlarmRealm alarmRealm = Realm.getDefaultInstance().createObject(AlarmRealm.class);

        alarmRealm.setId(generateId());
        alarmRealm.setAlarmHour(getAlarmHour());
        alarmRealm.setAlarmDay(getAlarmDay());
        alarmRealm.setStartPoint(getStartPoint());
        alarmRealm.setDestinationPoint(getDestinationPoint());
        alarmRealm.setDayOfWeek(getDayOfWeek());
        alarmRealm.setLngLatPointsRealmList(getLngLatPointsRealmList());
        Realm.getDefaultInstance().commitTransaction();
    }



    // aktualizacja alarmu w bazie
    public void updateAlarm(final AlarmRealm alarm) {
        AlarmRealm alarmRealm = Realm.getDefaultInstance().where(AlarmRealm.class).equalTo("id", alarm.getId()).findFirst();
        Realm.getDefaultInstance().beginTransaction();

        //wprowadzenie zmian w alarmie: tu zmiana godziny
        alarmRealm.setAlarmHour(getAlarmHour());
        alarmRealm.setAlarmDay(getAlarmDay());
        alarmRealm.setStartPoint(getStartPoint());
        alarmRealm.setDestinationPoint(getDestinationPoint());
        alarmRealm.setDayOfWeek(getDayOfWeek());
        alarmRealm.setLngLatPointsRealmList(getLngLatPointsRealmList());
        Realm.getDefaultInstance().commitTransaction();


        Realm.getDefaultInstance().commitTransaction();
    }


    public void setToNull(){

        setId(0);
        setAlarmHour(null);
        setAlarmDay(null);
        setDayOfWeek(null);
        setStartPoint(null);
        setDestinationPoint(null);
        setLngLatPointsRealmList(null);

    };

    //generowanie id
    private int generateId() {
        return Realm.getDefaultInstance().where(AlarmRealm.class).max("id").intValue() + 1;
    }


}
