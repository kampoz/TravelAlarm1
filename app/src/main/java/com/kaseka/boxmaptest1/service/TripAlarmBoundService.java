package com.kaseka.boxmaptest1.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.kaseka.boxmaptest1.global.GoogleTransportMode;
import com.kaseka.boxmaptest1.activity.MainActivity;
import com.kaseka.boxmaptest1.helper.GoogleDirectionsHelper;
import com.kaseka.boxmaptest1.helper.Parser;
import com.kaseka.boxmaptest1.listener.OnResponseListener;
import com.kaseka.boxmaptest1.networking.GetRouteDetailsRequest;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

public class TripAlarmBoundService extends Service {

    private String fromLocationId = "ChIJYUAVHhRXIkcRX-no9nruKFU";
    private String toLocationId = "ChIJ36UeUliaI0cR9vky0FB9vlI";
    GetRouteDetailsRequest getRouteDetailsRequest;
    private ArrayList<LatLng> responsePoints = new ArrayList<>();
    String routeTime;
    private int routeTimeInSeconds = 0;
    private String transportMode = "driving";


    private final IBinder binder = new TripAlarmBinder();

    public class TripAlarmBinder extends Binder{
        public TripAlarmBoundService getService(){
            return TripAlarmBoundService.this;
        }
    }

    @Override
    public void onCreate(){
        setRequest();
        //------startowanie activity
        Intent dialogIntent = new Intent(TripAlarmBoundService.this, MainActivity.class);
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
