package com.kaseka.boxmaptest1.helper;


import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;

public class Cache {

    private static AlarmPOJO alarmPOJO;

    public static AlarmPOJO clearAlarmPOJO() {
        Cache.alarmPOJO = new AlarmPOJO();
        return getAlarmPOJO();
    }

    public static AlarmPOJO getAlarmPOJO() {
        return alarmPOJO;
    }
}
