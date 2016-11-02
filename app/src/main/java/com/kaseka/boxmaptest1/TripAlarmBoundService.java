package com.kaseka.boxmaptest1;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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


    private final IBinder binder = new TripAlarmBinder();

    public class TripAlarmBinder extends Binder{
        public TripAlarmBoundService getService(){
            return TripAlarmBoundService.this;
        }
    }

    @Override
    public void onCreate(){

        setRequest();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void setRequest(){
        getRouteDetailsRequest = new GetRouteDetailsRequest(this, fromLocationId, toLocationId);
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
