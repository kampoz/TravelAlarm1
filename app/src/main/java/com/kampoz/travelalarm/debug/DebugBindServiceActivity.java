package com.kampoz.travelalarm.debug;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.kampoz.travelalarm.R;
import com.kampoz.travelalarm.service.AlarmBoundService;

public class DebugBindServiceActivity extends AppCompatActivity {

    private AlarmBoundService tripAlarmBoundService;
    private boolean bound = false;
    private String routeTime = " wartość zero";

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            AlarmBoundService.TripAlarmBinder tripAlarmBinder =
                    (AlarmBoundService.TripAlarmBinder) binder;
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
        Intent intent = new Intent(this, AlarmBoundService.class);
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
