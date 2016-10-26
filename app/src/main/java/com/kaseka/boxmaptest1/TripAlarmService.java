package com.kaseka.boxmaptest1;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaseka.boxmaptest1.debug.DebugActivity;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONObject;

import java.util.ArrayList;

public class TripAlarmService extends IntentService {

    public static final String EXTENDED_DATA_STATUS = "com.example.android.threadsample.STATUS";
    public static final String BROADCAST_ACTION = "com.example.android.threadsample.BROADCAST";
    public static final String EXTRA_MESSAGE = "extra message";
    private Handler handler;
    //private final IBinder mBinder = new LocalBinder();

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCFa5n3POS1VSsNgn8NKORx8pGfLSTYBGU";
    private ArrayList<LatLng> responsePoints = new ArrayList<>();
    private ScrollView scrollView;
    private MapView mapView;
    private MapboxMap map;
    private MarkerView markerViewFrom;
    private MarkerView markerViewTo;
    private Button bStart;
    private Button bDalej;
    private TextView tvRouteTime;
    private String fromLocationId = "ChIJYUAVHhRXIkcRX-no9nruKFU";
    private String toLocationId = "ChIJ36UeUliaI0cR9vky0FB9vlI";
    String routeTime;
    int routeTimeInSeconds = 0;
    GetRouteDetailsRequest getRouteDetailsRequest;



    public TripAlarmService() {
        super("TripAlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            for(int i=0; i<10; i++){

                Thread.sleep(5000);

                String text = intent.getStringExtra(EXTRA_MESSAGE);
                showtext(text);
                Log.d("Service1","onHandleIntent()"+i);
                setRequest();

            }
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        // Do work here, based on the contents of dataString
    }

    private void showtext(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRequest(){
        getRouteDetailsRequest = new GetRouteDetailsRequest(this, fromLocationId, toLocationId);
        getRouteDetailsRequest.setOnResponseListener(new OnResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("TripAlarmService","onSuccess");
                responsePoints = GoogleDirectionsHelper.decodePoly(Parser.parseRoutePoints(response));//= Parser.parseDirections(response);
                routeTime= Parser.parseWholeRouteTime(response);
                Log.d("TripAlarmService","czas przejazdu "+ routeTime);
                routeTimeInSeconds = Parser.parseRouteTimeInSekonds(response);
            }

            @Override
            public void onFailure() {
                Log.d("TripAlarmService","bład requesta w setRequest()");
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
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    public class LocalBinder extends Binder {
        public TripAlarmService getService() {
            return TripAlarmService.this;
        }
    }
}