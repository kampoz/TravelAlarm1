package com.kaseka.boxmaptest1;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// Broadcast receiver for receiving status updates from the IntentService
public class ResponseReceiver extends BroadcastReceiver
{
    // Prevents instantiation
    public ResponseReceiver() {
    }
    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
