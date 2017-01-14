package com.kaseka.boxmaptest1.test;

import android.util.Log;

import com.kaseka.boxmaptest1.data.realm.AlarmRealm;
import com.kaseka.boxmaptest1.global.DayOfWeek;
import com.kaseka.boxmaptest1.global.GoogleTransportMode;

import org.joda.time.DateTime;

import io.realm.Realm;


public class TestClass {


    public void createTestalarm(){
        final AlarmRealm alarmRealm = new AlarmRealm();
        long newId = 999;

        DateTime dt = new DateTime();
        long currentTimeMillis = System.currentTimeMillis();
        long testActivateTimeInMillis = currentTimeMillis + 5000;

        alarmRealm.setIsOn(true);
        alarmRealm.setId(newId);
        alarmRealm.setAlarmTimeInMillis(testActivateTimeInMillis);

        alarmRealm.setAlarmHour(2);
        alarmRealm.setAlarmMinute(2);
        alarmRealm.setAmPm(0);
        alarmRealm.setAlarmDayOfWeek(String.valueOf(DayOfWeek.MONDAY));
        alarmRealm.setAlarmDayOfWeekAsInt(1);
        alarmRealm.setStartPoint("honolulu");
        alarmRealm.setDestinationPoint("Cedynia");
        alarmRealm.setRouteTimeInSeconds(15);
        alarmRealm.setRouteTimeLabel("Label time");
        alarmRealm.setPreparingTimeInMins(33);
        alarmRealm.setLngLatPointsRealmList(null);
        alarmRealm.setAlarmDateTimeData(null);
        alarmRealm.setTransportMode(String.valueOf(GoogleTransportMode.bicycling));
        alarmRealm.setGoalHourOfDay(15);
        alarmRealm.setGoalMinute(23);
        alarmRealm.setFromLocationId("ChIJYUAVHhRXIkcRX-no9nruKFU");
        alarmRealm.setToLocationId("ChIJ0Z8CIhKfGEcRZNc472Kr49s");

        Realm defaultInstance = Realm.getDefaultInstance();
        defaultInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(alarmRealm);
            }
        });

        AlarmRealm currentAlarmRealm = defaultInstance.where(AlarmRealm.class).equalTo("id", newId).findFirst();
        //Toast.makeText(SplashActivity.this, String.valueOf(currentAlarmRealm.getId())+" "+String.valueOf(currentAlarmRealm.getAlarmTimeInMillis()), Toast.LENGTH_LONG).show();
        Log.d("createTestalarm","currentAlarmRealm id:" +currentAlarmRealm.getId());
        Log.d("createTestalarm","current time:" +dt.getMillis());
        Log.d("createTestalarm","currentAlarmRealm time in millis:" +currentAlarmRealm.getAlarmTimeInMillis());
        Log.d("createTestalarm","currentAlarmRealm difference:" + (currentAlarmRealm.getAlarmTimeInMillis()-currentAlarmRealm.getAlarmTimeInMillis()));
    }
}
