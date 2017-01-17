package com.kampoz.travelalarm.helper;


import com.kampoz.travelalarm.data.realm.AlarmPOJO;

public class Cache {

    private static AlarmPOJO alarmPOJO;

//    public static void setAlarmPOJO( AlarmPOJO alarmPOJO ){
//        Cache.alarmPOJO = alarmPOJO;
//    }

    public static AlarmPOJO clearAlarmPOJO() {
        Cache.alarmPOJO = new AlarmPOJO();
        return getAlarmPOJO();
    }

    public static AlarmPOJO getAlarmPOJO() {
        return alarmPOJO;
    }
}
