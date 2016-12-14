package com.kaseka.boxmaptest1.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.kaseka.boxmaptest1.global.GoogleTransportMode;
import com.kaseka.boxmaptest1.activity.AlarmActivity;
import com.kaseka.boxmaptest1.helper.GoogleDirectionsHelper;
import com.kaseka.boxmaptest1.helper.Parser;
import com.kaseka.boxmaptest1.listener.OnResponseListener;
import com.kaseka.boxmaptest1.networking.GetRouteDetailsRequest;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

public class TripAlarmStartedService extends IntentService {

    public static final String EXTENDED_DATA_STATUS = "com.example.android.threadsample.STATUS";
    public static final String BROADCAST_ACTION = "com.example.android.threadsample.BROADCAST";
    public static final String EXTRA_MESSAGE = "extra message";
    private Handler handler;
    private String transportMode = "driving";
    //private final IBinder mBinder = new LocalBinder();

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCFa5n3POS1VSsNgn8NKORx8pGfLSTYBGU";
    private ArrayList<LatLng> responsePoints = new ArrayList<>();

    private String fromLocationId = "ChIJYUAVHhRXIkcRX-no9nruKFU";
    private String toLocationId = "ChIJ36UeUliaI0cR9vky0FB9vlI";
    String routeTime;
    int routeTimeInSeconds = 0;
    GetRouteDetailsRequest getRouteDetailsRequest;



    public TripAlarmStartedService() {
        super("TripAlarmStartedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //------startowanie activity
        //próne uśnpienie startu na 5s.
//        try {
//            Thread.sleep(8000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        Intent dialogIntent = new Intent(TripAlarmStartedService.this, AlarmActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);

        try {
            for(int i=0; i<10; i++){

                Thread.sleep(5000);

                String text = intent.getStringExtra(EXTRA_MESSAGE);
                //showtext(text);
                //setRequest();
                Log.d("Service1","onHandleIntent()"+i);

                getRouteTime();
            }
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

    }

    private void getRouteTime() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                setRequest();
                Toast.makeText(getApplicationContext(), routeTime, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRequest(){
        getRouteDetailsRequest = new GetRouteDetailsRequest(this, fromLocationId, toLocationId, GoogleTransportMode.bicycling);
        getRouteDetailsRequest.setOnResponseListener(new OnResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("TripAlarmStartedService","onSuccess");
                responsePoints = GoogleDirectionsHelper.decodePoly(Parser.parseRoutePoints(response));//= Parser.parseDirections(response);
                routeTime= Parser.parseWholeRouteTime(response);
                Log.d("TripAlarmStartedService","czas przejazdu "+ routeTime);
                routeTimeInSeconds = Parser.parseRouteTimeInSekonds(response);
            }

            @Override
            public void onFailure() {
                Log.d("TripAlarmStartedService","bład requesta w setRequest()");
            }
        });
        getRouteDetailsRequest.execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.v("onUnbind", "in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        Log.d("onDestroy", "onDestroy");
        Toast.makeText(this, "Service done", Toast.LENGTH_SHORT).show();
    }

    public class LocalBinder extends Binder {
        public TripAlarmStartedService getService() {
            return TripAlarmStartedService.this;
        }
    }
}