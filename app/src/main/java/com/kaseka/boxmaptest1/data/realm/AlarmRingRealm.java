package com.kaseka.boxmaptest1.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wasili on 2017-01-08.
 */

public class AlarmRingRealm extends RealmObject {

    @PrimaryKey
    private long id;
    private int soundId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }
}
