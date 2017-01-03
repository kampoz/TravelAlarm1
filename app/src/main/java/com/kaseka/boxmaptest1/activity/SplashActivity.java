package com.kaseka.boxmaptest1.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.data.realm.AlarmRealm;

import org.joda.time.DateTime;

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

        new MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            DateTime dateTime1 = new DateTime();
            return null;
        }
    }

}
