package com.kaseka.boxmaptest1.debug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.service.AlarmActivateStartedService;

public class DebugStartedServiceActivity extends AppCompatActivity {

    AlarmActivateStartedService tripAlarmService;
    boolean bound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        Intent intent = new Intent(this, AlarmActivateStartedService.class);
        intent.putExtra(AlarmActivateStartedService.EXTRA_MESSAGE, "extra message");
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


