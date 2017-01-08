package com.kaseka.boxmaptest1.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wasili on 2017-01-08.
 */

public class AlarmRingRealm extends RealmObject {

    @PrimaryKey
    long id;
    String activeAlarmRing;




    public String getActiveAlarmRing() {
        return activeAlarmRing;
    }

    public void setActiveAlarmRing(String activeAlarmRing) {
        this.activeAlarmRing = activeAlarmRing;
    }
}
