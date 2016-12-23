package com.kaseka.boxmaptest1.data.realm;


import org.joda.time.DateTime;

import io.realm.Realm;
import io.realm.RealmList;

public class AlarmPOJO {

    private static long id;
    private static boolean isOn = false;
    private static boolean isAlarmPeriodic = false;
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
    private static RealmList<LatLngRealm> lngLatPointsRealmList = new RealmList<>();
    private static String alarmDateTimeData;
    private static String transportMode;
    private static String goalHourOfDay;
    private static String goalMinute;



    public static String getAlarmDayOfWeek() {
        return alarmDayOfWeek;
    }

    public static void setAlarmDayOfWeek(String alarmDayOfWeek) {
        AlarmPOJO.alarmDayOfWeek = alarmDayOfWeek;
    }

    public static boolean isOn() {
        return isOn;
    }

    public static boolean isAlarmPeriodic() {
        return isAlarmPeriodic;
    }

    public static void setIsAlarmPeriodic(boolean isAlarmPeriodic) {
        AlarmPOJO.isAlarmPeriodic = isAlarmPeriodic;
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

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        AlarmPOJO.id = id;
    }

    public static boolean getIsOn() {
        return isOn;
    }

    public static void setIsOn(boolean isOn) {
        AlarmPOJO.isOn = isOn;
    }

    public static RealmList<LatLngRealm> getLngLatPointsRealmList() {
        return lngLatPointsRealmList;
    }

    public static void setLngLatPointsRealmList(RealmList<LatLngRealm> lngLatPointsRealmList) {
        AlarmPOJO.lngLatPointsRealmList = lngLatPointsRealmList;
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


    public static String getAlarmDateTimeData() {
        return alarmDateTimeData;
    }

    public static void setAlarmDateTimeData(String alarmDateTimeData) {
        AlarmPOJO.alarmDateTimeData = alarmDateTimeData;
    }

    public static String getTransportMode() {
        return transportMode;
    }

    public static void setTransportMode(String transportMode) {
        AlarmPOJO.transportMode = transportMode;
    }

    public static String getGoalHourOfDay() {
        return goalHourOfDay;
    }

    public static void setGoalHourOfDay(String goalHourOfDay) {
        AlarmPOJO.goalHourOfDay = goalHourOfDay;
    }

    public static String getGoalMinute() {
        return goalMinute;
    }

    public static void setGoalMinute(String goalMinute) {
        AlarmPOJO.goalMinute = goalMinute;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //dodanie nowego alarmu
    public static void insertAlarmToRealm(){
//        Realm defaultInstance = Realm.getDefaultInstance();
//
//        defaultInstance.beginTransaction();

        final AlarmRealm alarmRealm = new AlarmRealm();

        //int newId = defaultInstance.where(AlarmRealm.class).max("id").intValue() + 1;
        long newId = 10;

        alarmRealm.setId( newId );

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
        alarmRealm.setAlarmDateTimeData(getAlarmDateTimeData());
        alarmRealm.setTransportMode(getTransportMode());
        alarmRealm.setGoalHourOfDay(getGoalHourOfDay());
        alarmRealm.setGoalMinute(getGoalMinute());

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(alarmRealm);
            }
        });
        Realm.getDefaultInstance().close();

        //defaultInstance.createObject(AlarmRealm.class);
        //defaultInstance.commitTransaction();
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
        alarmRealm.setAlarmDateTimeData(getAlarmDateTimeData());
        alarmRealm.setTransportMode(getTransportMode());
        alarmRealm.setGoalHourOfDay(getGoalHourOfDay());
        alarmRealm.setGoalMinute(getGoalMinute());

        Realm.getDefaultInstance().commitTransaction();

    }


    public static void setToNull(){

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
        lngLatPointsRealmList = new RealmList<>();
        setAlarmDateTimeData(null);
        setTransportMode(null);

    };

    //generowanie id
    private int generateId() {
        return Realm.getDefaultInstance().where(AlarmRealm.class).max("id").intValue() + 1;
    }


}