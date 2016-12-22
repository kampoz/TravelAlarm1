package com.kaseka.boxmaptest1.debug;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.service.TripAlarmBoundService;

public class DebugBindServiceActivity extends AppCompatActivity {

    private TripAlarmBoundService tripAlarmBoundService;
    private boolean bound = false;
    private String routeTime = " wartość zero";

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            TripAlarmBoundService.TripAlarmBinder tripAlarmBinder =
                    (TripAlarmBoundService.TripAlarmBinder) binder;
            tripAlarmBoundService = tripAlarmBinder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    public void getRouteTime() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

//                try {
//                Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                if (tripAlarmBoundService != null) {
                    routeTime = String.valueOf(tripAlarmBoundService.getRouteTimeInSeconds());
                }
                Log.d("getRouteTimeLabel()",routeTime);
                Toast.makeText(getApplicationContext(), routeTime, Toast.LENGTH_SHORT).show();

                //---startowanie activity


                //-------------

                handler.postDelayed(this, 3000);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_bind_service);
        getRouteTime();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, TripAlarmBoundService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (bound){
            unbindService(connection);
            bound = false;
        }
    }
}
