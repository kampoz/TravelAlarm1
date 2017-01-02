package com.kaseka.boxmaptest1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.data.realm.AlarmRealm;

import java.util.Arrays;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //getSupportActionBar().hide();

        Realm.getDefaultInstance().beginTransaction();
        Realm.getDefaultInstance().deleteAll();
        Realm.getDefaultInstance().commitTransaction();

        setTestRealmDataBase();

        Realm realm = Realm.getDefaultInstance();

        if(realm.where(AlarmRealm.class).count() > 0){
            Log.d("SplashActivity", "IS NOT EMPTY");
            Intent startAlarmsListActivityIntent = new Intent(this, AlarmsListActivity.class);
            this.startActivity(startAlarmsListActivityIntent);
        }
        else
        {
            Log.d("SplashActivity", "IS EMPTY");
            Intent startMainActivityIntent = new Intent(this, MainActivity.class);
            this.startActivity(startMainActivityIntent);
        }
    }

    private void setTestRealmDataBase() {
        for (int i = 0; i < 10; ++i) {
            final AlarmRealm alarmRealm = new AlarmRealm();
            alarmRealm.setId(i);
            alarmRealm.setAlarmDayOfWeek(String.valueOf(i));
            alarmRealm.setAlarmHour(15);
            alarmRealm.setDestinationPoint("do Lublina");

            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(alarmRealm);
                }
            });
            Realm.getDefaultInstance().close();
            //alarms.add(alarmRealm);
        }
    }
}
