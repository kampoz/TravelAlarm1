package com.kaseka.boxmaptest1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.data.realm.AlarmRealm;
import com.kaseka.boxmaptest1.data.realm.AlarmRingRealm;
import com.kaseka.boxmaptest1.service.AlarmActivateStartedService;
import com.kaseka.boxmaptest1.service.AlarmsUpdateService;
import com.kaseka.boxmaptest1.test.TestClass;

import org.joda.time.DateTime;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Animation anim2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //getSupportActionBar().hide();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        setFirstAlarmSound();

        Intent intent = new Intent(this, AlarmActivateStartedService.class);
        startService(intent);

        Intent intent2 = new Intent(this, AlarmsUpdateService.class);
        startService(intent2);

        final ImageView startIcon = (ImageView) findViewById(R.id.start_icon_test);
        ViewTreeObserver vto = startIcon.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                anim2 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.anim2);
                startIcon.startAnimation(anim2);

                anim2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        startingCorrectActivity();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        new startingJodaDateTimeAsyncTask().execute();
    }

    private class startingJodaDateTimeAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            DateTime dateTime1 = new DateTime();
            dateTime1 = null;
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_alarm) {
            Intent startAlarmsListActivityIntent = new Intent(this, MainActivity.class);
            this.startActivity(startAlarmsListActivityIntent);

        }

        if (id == R.id.action_show_alarms_list) {
            Intent startAlarmsListActivityIntent = new Intent(this, AlarmsListActivity.class);
            this.startActivity(startAlarmsListActivityIntent);

        }

        if (id == R.id.action_setting) {
            Intent startSettingsActivityIntent = new Intent(this, SettingsActivity.class);
            this.startActivity(startSettingsActivityIntent);

        }

        if (id == R.id.action_about) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Copyright \u00a9 2017\nKamil Poznakowski\nkampoznak@gmail.com");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


    private void startingCorrectActivity() {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(AlarmRealm.class).count() > 0) {
            Log.d("SplashActivity", "IS NOT EMPTY");

            //Testowy alarm
            TestClass testClass = new TestClass();
            testClass.createTestalarm();

            Intent startAlarmsListActivityIntent = new Intent(this, AlarmsListActivity.class);
            this.startActivity(startAlarmsListActivityIntent);
        } else {
            Log.d("SplashActivity", "IS EMPTY");
            TestClass testClass = new TestClass();
            testClass.createTestalarm();
            Toast.makeText(this, "No alarms", Toast.LENGTH_LONG).show();
            Intent startMainActivityIntent = new Intent(this, MainActivity.class);
            this.startActivity(startMainActivityIntent);
        }
        realm = null;
    }

    private void setFirstAlarmSound() {
        Realm realm = Realm.getDefaultInstance();
        if (!(realm.where(AlarmRingRealm.class).count() > 0)) {
            realm = Realm.getDefaultInstance();
            final AlarmRingRealm alarmRingRealm = new AlarmRingRealm();
            alarmRingRealm.setId(1);
            alarmRingRealm.setSoundId(R.raw.sound3);
            alarmRingRealm.setSoundName("Sound 3");
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(alarmRingRealm);
                }
            });
            realm = null;
        }
    }
}
