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
import android.widget.Toast;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.data.realm.AlarmRealm;

import org.joda.time.DateTime;

import java.util.Arrays;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //getSupportActionBar().hide();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        Realm realm = Realm.getDefaultInstance();


        if(realm.where(AlarmRealm.class).count() > 0){
            Log.d("SplashActivity", "IS NOT EMPTY");
            Intent startAlarmsListActivityIntent = new Intent(this, AlarmsListActivity.class);
            this.startActivity(startAlarmsListActivityIntent);
        }
        else
        {
            Log.d("SplashActivity", "IS EMPTY");
            Toast.makeText(this, "No alarms", Toast.LENGTH_LONG).show();
            Intent startMainActivityIntent = new Intent(this, MainActivity.class);
            this.startActivity(startMainActivityIntent);
        }

        new MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
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
        if (id==R.id.action_add_alarm){
            Intent startAlarmsListActivityIntent = new Intent(this, MainActivity.class);
            this.startActivity(startAlarmsListActivityIntent);
            this.finish();
        }

        if (id==R.id.action_show_alarms_list){
            Intent startAlarmsListActivityIntent = new Intent(this, AlarmsListActivity.class);
            this.startActivity(startAlarmsListActivityIntent);
            this.finish();
        }

        if (id==R.id.action_setting){

        }

        if (id==R.id.action_about){
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

}
