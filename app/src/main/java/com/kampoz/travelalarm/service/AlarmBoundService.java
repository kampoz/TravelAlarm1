package com.kampoz.travelalarm.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.kampoz.travelalarm.activity.MainActivity;
import com.kampoz.travelalarm.global.GoogleTransportMode;
import com.kampoz.travelalarm.helper.GoogleDirectionsHelper;
import com.kampoz.travelalarm.helper.Parser;
import com.kampoz.travelalarm.listener.OnResponseListener;
import com.kampoz.travelalarm.networking.GetRouteDetailsRequest;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

public class AlarmBoundService extends Service {

    private String fromLocationId = "ChIJYUAVHhRXIkcRX-no9nruKFU";
    private String toLocationId = "ChIJ36UeUliaI0cR9vky0FB9vlI";
    GetRouteDetailsRequest getRouteDetailsRequest;
    private ArrayList<LatLng> responsePoints = new ArrayList<>();
    String routeTime;
    private int routeTimeInSeconds = 0;
    private String transportMode = "driving";


    private final IBinder binder = new TripAlarmBinder();

    public class TripAlarmBinder extends Binder{
        public AlarmBoundService getService(){
            return AlarmBoundService.this;
        }
    }

    @Override
    public void onCreate(){
        setRequest();
        //------startowanie activity
        Intent dialogIntent = new Intent(AlarmBoundService.this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);

    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void setRequest(){
        getRouteDetailsRequest = new GetRouteDetailsRequest(this, fromLocationId, toLocationId, GoogleTransportMode.bicycling);
        getRouteDetailsRequest.setOnResponseListener(new OnResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("TripAlarmBindService","onSuccess");
                responsePoints = GoogleDirectionsHelper.decodePoly(Parser.parseRoutePoints(response));//= Parser.parseDirections(response);
                routeTime= Parser.parseWholeRouteTime(response);
                Log.d("TripAlarmBindService","czas przejazdu "+ routeTime);
                routeTimeInSeconds = Parser.parseRouteTimeInSekonds(response);


            }

            @Override
            public void onFailure() {
                Log.d("TripAlarmBindService","bład requesta w setRequest()");
            }
        });
        getRouteDetailsRequest.execute();
    }


    public int getRouteTimeInSeconds() {
        return routeTimeInSeconds;
    }
}
