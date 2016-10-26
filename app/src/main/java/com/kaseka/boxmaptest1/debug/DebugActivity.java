package com.kaseka.boxmaptest1.debug;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.TripAlarmService;

public class DebugActivity extends AppCompatActivity {
    Intent mServiceIntent;
    TripAlarmService tripAlarmService;
    boolean bound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        Intent intent = new Intent(this, TripAlarmService.class);
        intent.putExtra(TripAlarmService.EXTRA_MESSAGE, "extra message");
        //bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);

    }



//    @Override
//    protected void onStart(){
//        super.onStart();
//
//    }
//
//
//    @Override
//    protected void onStop(){
//        super.onStop();
//        /*if (bound){
//            unbindService(mServiceConnection);
//            bound = false;
//        }*/
//    }

}


