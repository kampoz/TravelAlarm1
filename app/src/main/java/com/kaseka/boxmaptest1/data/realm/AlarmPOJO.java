package com.kaseka.boxmaptest1.data.realm;


import io.realm.Realm;
import io.realm.RealmList;


public class AlarmPOJO {

    private long id;
    private boolean isOn = false;
    private boolean isAlarmPeriodic = false;
    private int alarmHour;
    private int alarmMinute;
    private int amPm;
    private String alarmDayOfWeek;
    private int alarmDayOfWeekAsInt;
    private String startPoint;
    private String destinationPoint;
    private String routeTimeLabel;
    private int routeTimeInSeconds = 0;
    private Integer preparingTimeInMins;
    private Long alarmTimeInMillis;
    private RealmList<LatLngRealm> lngLatPointsRealmList = new RealmList<>();
    private String alarmDateTimeData;
    private String transportMode;
    private int goalHourOfDay;
    private int goalMinute;
    private String fromLocationId;
    private String toLocationId;

    public void setFirstPhaseData(String routeTimeLabel, int routeTimeInSeconds,
                                  String startPoint,
                                  String destinationPoint, String transportMode,
                                  String fromLocationId, String toLocationId){
        setRouteTimeLabel(routeTimeLabel);
        setRouteTimeInSeconds(routeTimeInSeconds);
        setStartPoint(startPoint);
        setDestinationPoint(destinationPoint);
        setTransportMode(transportMode.toString());
        setFromLocationId(fromLocationId);
        setToLocationId(toLocationId);
    }

    public void setSecondPhaseData (int alarmHour, int alarmMinute, String alarmDayOfWeek, Integer preparingTimeInMins,
                                    int amPm, Long alarmTimeInMillis, String alarmDateTimeData, int goalHourOfDay, int goalMinute){
        setIsOn(true);
        setAlarmHour(alarmHour);
        setAlarmMinute(alarmMinute);
        setAlarmDayOfWeek(alarmDayOfWeek);
        setPreparingTimeInMins(preparingTimeInMins);
        setAmPm(amPm);
        setAlarmTimeInMillis(alarmTimeInMillis);
        setAlarmDateTimeData(alarmDateTimeData.toString());
        setGoalHourOfDay(goalHourOfDay);
        setGoalMinute(goalMinute);
    }


    public String getAlarmDayOfWeek() {
        return alarmDayOfWeek;
    }

    private void setAlarmDayOfWeek(String alarmDayOfWeek) {
        this.alarmDayOfWeek = alarmDayOfWeek;
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean isAlarmPeriodic() {
        return isAlarmPeriodic;
    }

    public void setIsAlarmPeriodic(boolean isAlarmPeriodic) {
        this.isAlarmPeriodic = isAlarmPeriodic;
    }

    public int getAlarmHour() {
        return alarmHour;
    }

    private void setAlarmHour(int alarmHour) {
        this.alarmHour = alarmHour;
    }

    public int getAlarmMinute() {
        return alarmMinute;
    }

    private void setAlarmMinute(int alarmMinute) {
        this.alarmMinute = alarmMinute;
    }

    public Long getAlarmTimeInMillis() {
        return alarmTimeInMillis;
    }

    private void setAlarmTimeInMillis(Long alarmTimeInMillis) {
        this.alarmTimeInMillis = alarmTimeInMillis;
    }

    public int getAmPm() {
        return amPm;
    }

    private void setAmPm(int amPm) {
        this.amPm = amPm;
    }

    public String getDestinationPoint() {
        return destinationPoint;
    }

    private void setDestinationPoint(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsOn() {
        return isOn;
    }

    private void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

    public RealmList<LatLngRealm> getLngLatPointsRealmList() {
        return lngLatPointsRealmList;
    }

    public void setLngLatPointsRealmList(RealmList<LatLngRealm> lngLatPointsRealmList) {
        this.lngLatPointsRealmList = lngLatPointsRealmList;
    }

    public Integer getPreparingTimeInMins() {
        return preparingTimeInMins;
    }

    private void setPreparingTimeInMins(Integer preparingTimeInMins) {
        this.preparingTimeInMins = preparingTimeInMins;
    }

    public String getRouteTimeLabel() {
        return routeTimeLabel;
    }

    private void setRouteTimeLabel(String routeTimeLabel) {
        this.routeTimeLabel = routeTimeLabel;
    }

    public String getStartPoint() {
        return startPoint;
    }

    private void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }


    public int getRouteTimeInSeconds() {
        return routeTimeInSeconds;
    }

    private void setRouteTimeInSeconds(int routeTimeInSeconds) {
        this.routeTimeInSeconds = routeTimeInSeconds;
    }


    public int getAlarmDayOfWeekAsInt() {
        return alarmDayOfWeekAsInt;
    }

    public void setAlarmDayOfWeekAsInt(int alarmDayOfWeekAsInt) {
        this.alarmDayOfWeekAsInt = alarmDayOfWeekAsInt;
    }


    public String getAlarmDateTimeData() {
        return alarmDateTimeData;
    }

    private void setAlarmDateTimeData(String alarmDateTimeData) {
        this.alarmDateTimeData = alarmDateTimeData;
    }

    public String getTransportMode() {
        return transportMode;
    }

    private void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public int getGoalHourOfDay() {
        return goalHourOfDay;
    }

    private void setGoalHourOfDay(int goalHourOfDay) {
        this.goalHourOfDay = goalHourOfDay;
    }

    public int getGoalMinute() {
        return goalMinute;
    }

    private void setGoalMinute(int goalMinute) {
        this.goalMinute = goalMinute;
    }


    public String getFromLocationId() {
        return fromLocationId;
    }

    private void setFromLocationId(String fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public String getToLocationId() {
        return toLocationId;
    }

    private void setToLocationId(String toLocationId) {
        this.toLocationId = toLocationId;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //dodanie nowego alarmu
    public void insertAlarmToRealm() {

        final AlarmRealm alarmRealm = new AlarmRealm();
        long newId;

        Realm defaultInstance = Realm.getDefaultInstance();
        Number oldMaxId = defaultInstance.where(AlarmRealm.class).max("id");

        if (oldMaxId==null){
            newId = 1;
        }
        else{
        newId = oldMaxId.intValue() + 1;
        }
        //Log.d("defaultInstanceID", "max id: " + oldMaxId.toString());
        //Log.d("defaultInstanceID", "newId: " + newId);

        alarmRealm.setIsOn(getIsOn());
        alarmRealm.setId(newId);
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
        alarmRealm.setFromLocationId(getFromLocationId());
        alarmRealm.setToLocationId(getToLocationId());

        defaultInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(alarmRealm);
            }
        });
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
        alarmRealm.setFromLocationId(getFromLocationId());
        alarmRealm.setToLocationId(getToLocationId());

        Realm.getDefaultInstance().commitTransaction();

    }


    public void setToNull() {

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
        setGoalHourOfDay(0);
        setGoalMinute(0);
        setFromLocationId(null);
        setToLocationId(null);
    }

    //ma uzupełniać statyczny alarmPOJO pobranymi danymi z obiektu AlarmRealm - do wyswietlenie na AlarmsLIst jako Dialog
    public void setAlarmPOJODataFromAlarmRealm(AlarmRealm alarmRealm){
        setId(alarmRealm.getId());
        setIsOn(alarmRealm.getIsOn());
        setAlarmHour(alarmRealm.getAlarmHour());
        setAlarmMinute(alarmRealm.getAlarmMinute());
        setAmPm(alarmRealm.getAmPm());
        setAlarmDayOfWeek(alarmRealm.getAlarmDayOfWeek());
        setAlarmDayOfWeekAsInt(alarmRealm.getAlarmDayOfWeekAsInt());
        setStartPoint(alarmRealm.getStartPoint());
        setDestinationPoint(alarmRealm.getDestinationPoint());
        setRouteTimeInSeconds(alarmRealm.getRouteTimeInSeconds());
        setRouteTimeLabel(alarmRealm.getRouteTimeLabel());
        setPreparingTimeInMins(alarmRealm.getPreparingTimeInMins());
        setAlarmTimeInMillis(alarmRealm.getAlarmTimeInMillis());
        setLngLatPointsRealmList(alarmRealm.getLngLatPointsRealmList());
        setLngLatPointsRealmList(getLngLatPointsRealmList());
        setAlarmDateTimeData(alarmRealm.getAlarmDateTimeData());
        setTransportMode(alarmRealm.getTransportMode());
        setGoalHourOfDay(alarmRealm.getGoalHourOfDay());
        setGoalMinute(alarmRealm.getGoalMinute());
        setFromLocationId(alarmRealm.getFromLocationId());
        setToLocationId(alarmRealm.getToLocationId());
    }


    public void setIsOntoRealm(){
        final AlarmRealm alarmRealm = new AlarmRealm();
        Realm defaultInstance = Realm.getDefaultInstance();
        alarmRealm.setIsOn(getIsOn());
        defaultInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(alarmRealm);
            }
        });
    }

}
