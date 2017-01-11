package com.kaseka.boxmaptest1.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.kaseka.boxmaptest1.receiver.ResponseReceiver;
import com.kaseka.boxmaptest1.service.AlarmActivateStartedService;

public class DisplayActivity extends AppCompatActivity {

    public void onCreate(Bundle stateBundle) {

        super.onCreate(stateBundle);

        // The filter's action is BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter(
                AlarmActivateStartedService.BROADCAST_ACTION);

        // Adds a data filter for the HTTP scheme
        mStatusIntentFilter.addDataScheme("http");

        ResponseReceiver mDownloadStateReceiver =
                new ResponseReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mDownloadStateReceiver,
                mStatusIntentFilter);
    }
}