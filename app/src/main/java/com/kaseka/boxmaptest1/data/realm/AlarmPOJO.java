package com.kaseka.boxmaptest1.data.realm;


import io.realm.Realm;
import io.realm.RealmList;

public class AlarmPOJO {

    private static int id;
    private static boolean isOn = false;
    private static int alarmHour;
    private static int alarmMinute;
    private static int amPm;

    private static String alarmDayOfWeek;
    private static int alarmDayOfWeekAsInt;
    private static String startPoint;
    private static String destinationPoint;
    private static String routeTimeLabel;
    private static int routeTimeInSeconds;
    private static Integer preparingTimeInMins;
    private static Long alarmTimeInMillis;

    private static RealmList<LatLngRealm> LngLatPointsRealmList = new RealmList<>();


    public static String getAlarmDayOfWeek() {
        return alarmDayOfWeek;
    }

    public static void setAlarmDayOfWeek(String alarmDayOfWeek) {
        AlarmPOJO.alarmDayOfWeek = alarmDayOfWeek;
    }

    public static int getAlarmHour() {
        return alarmHour;
    }

    public static void setAlarmHour(int alarmHour) {
        AlarmPOJO.alarmHour = alarmHour;
    }

    public static int getAlarmMinute() {
        return alarmMinute;
    }

    public static void setAlarmMinute(int alarmMinute) {
        AlarmPOJO.alarmMinute = alarmMinute;
    }

    public static Long getAlarmTimeInMillis() {
        return alarmTimeInMillis;
    }

    public static void setAlarmTimeInMillis(Long alarmTimeInMillis) {
        AlarmPOJO.alarmTimeInMillis = alarmTimeInMillis;
    }

    public static int getAmPm() {
        return amPm;
    }

    public static void setAmPm(int amPm) {
        AlarmPOJO.amPm = amPm;
    }

    public static String getDestinationPoint() {
        return destinationPoint;
    }

    public static void setDestinationPoint(String destinationPoint) {
        AlarmPOJO.destinationPoint = destinationPoint;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        AlarmPOJO.id = id;
    }

    public static boolean getIsOn() {
        return isOn;
    }

    public static void setIsOn(boolean isOn) {
        AlarmPOJO.isOn = isOn;
    }

    public static RealmList<LatLngRealm> getLngLatPointsRealmList() {
        return LngLatPointsRealmList;
    }

    public static void setLngLatPointsRealmList(RealmList<LatLngRealm> lngLatPointsRealmList) {
        LngLatPointsRealmList = lngLatPointsRealmList;
    }

    public static Integer getPreparingTimeInMins() {
        return preparingTimeInMins;
    }

    public static void setPreparingTimeInMins(Integer preparingTimeInMins) {
        AlarmPOJO.preparingTimeInMins = preparingTimeInMins;
    }

    public static String getRouteTimeLabel() {
        return routeTimeLabel;
    }

    public static void setRouteTimeLabel(String routeTimeLabel) {
        AlarmPOJO.routeTimeLabel = routeTimeLabel;
    }

    public static String getStartPoint() {
        return startPoint;
    }

    public static void setStartPoint(String startPoint) {
        AlarmPOJO.startPoint = startPoint;
    }




    public static int getRouteTimeInSeconds() {
        return routeTimeInSeconds;
    }

    public static void setRouteTimeInSeconds(int routeTimeInSeconds) {
        AlarmPOJO.routeTimeInSeconds = routeTimeInSeconds;
    }


    public static int getAlarmDayOfWeekAsInt() {
        return alarmDayOfWeekAsInt;
    }

    public static void setAlarmDayOfWeekAsInt(int alarmDayOfWeekAsInt) {
        AlarmPOJO.alarmDayOfWeekAsInt = alarmDayOfWeekAsInt;
    }

    //wstawianie nowego alarmu
    public void insertAlarm(final AlarmRealm alarm){
        Realm.getDefaultInstance().beginTransaction();
        AlarmRealm alarmRealm = Realm.getDefaultInstance().createObject(AlarmRealm.class);


        alarmRealm.setAlarmHour(getAlarmHour());
        alarmRealm.setAlarmMinute(getAlarmMinute());
        alarmRealm.setAmPm(getAmPm());
        alarmRealm.setAlarmDayOfWeek(getAlarmDayOfWeek());
        alarmRealm.setAlarmDayOfWeekAsInt(getAlarmDayOfWeekAsInt());
        alarmRealm.setStartPoint(getStartPoint());
        alarmRealm.setDestinationPoint(getDestinationPoint());
        alarmRealm.setRouteTimeInSeconds(getRouteTimeInSeconds());
        alarmRealm.setRouteTimeLabel(getRouteTimeLabel());
        alarmRealm.setPreparingTimeInMins(getPreparingTimeInMins());
        alarmRealm.setAlarmTimeInMillis(getAlarmTimeInMillis());
        alarmRealm.setLngLatPointsRealmList(getLngLatPointsRealmList());

        Realm.getDefaultInstance().commitTransaction();
    }



    // aktualizacja alarmu w bazie
    public void updateAlarm(final AlarmRealm alarm) {
        AlarmRealm alarmRealm = Realm.getDefaultInstance().where(AlarmRealm.class).equalTo("id", alarm.getId()).findFirst();
        Realm.getDefaultInstance().beginTransaction();

        //wprowadzenie zmian w alarmie: tu zmiana godziny
        alarmRealm.setIsOn(getIsOn());
        alarmRealm.setAlarmHour(getAlarmHour());
        alarmRealm.setAlarmMinute(getAlarmMinute());
        alarmRealm.setAmPm(getAmPm());
        alarmRealm.setAlarmDayOfWeek(getAlarmDayOfWeek());
        alarmRealm.setAlarmDayOfWeekAsInt(getAlarmDayOfWeekAsInt());
        alarmRealm.setStartPoint(getStartPoint());
        alarmRealm.setDestinationPoint(getDestinationPoint());
        alarmRealm.setRouteTimeInSeconds(getRouteTimeInSeconds());
        alarmRealm.setRouteTimeLabel(getRouteTimeLabel());
        alarmRealm.setPreparingTimeInMins(getPreparingTimeInMins());
        alarmRealm.setAlarmTimeInMillis(getAlarmTimeInMillis());
        alarmRealm.setLngLatPointsRealmList(getLngLatPointsRealmList());

        Realm.getDefaultInstance().commitTransaction();

    }


    public void setToNull(){

        setId(0);
        setIsOn(false);
        setAlarmHour(0);
        setAlarmMinute(0);
        setAmPm(0);
        setAlarmDayOfWeek(null);
        setAlarmDayOfWeekAsInt(getAlarmDayOfWeekAsInt());
        setStartPoint(null);
        setDestinationPoint(null);
        setRouteTimeInSeconds(0);
        setRouteTimeLabel(null);
        setPreparingTimeInMins(null);
        setAlarmTimeInMillis(null);
        setLngLatPointsRealmList(null);

    };

    //generowanie id
    private int generateId() {
        return Realm.getDefaultInstance().where(AlarmRealm.class).max("id").intValue() + 1;
    }


}
