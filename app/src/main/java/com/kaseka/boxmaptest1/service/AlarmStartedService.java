package com.kaseka.boxmaptest1.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.kaseka.boxmaptest1.data.realm.AlarmRealm;
import com.kaseka.boxmaptest1.global.GoogleTransportMode;
import com.kaseka.boxmaptest1.activity.AlarmActivity;
import com.kaseka.boxmaptest1.helper.GoogleDirectionsHelper;
import com.kaseka.boxmaptest1.helper.Parser;
import com.kaseka.boxmaptest1.listener.OnResponseListener;
import com.kaseka.boxmaptest1.networking.GetRouteDetailsRequest;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AlarmStartedService extends IntentService {

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

//    private String fromLocationId = "ChIJYUAVHhRXIkcRX-no9nruKFU";
//    private String toLocationId = "ChIJ36UeUliaI0cR9vky0FB9vlI";
    String routeTime;
    int routeTimeInSeconds = 0;
    GetRouteDetailsRequest getRouteDetailsRequest;

    private long oldAlarmTimeInMillis;
    private long oldTravelTimeInmillis;
    private long newTravelTimeInMillis;
    private long newAlarmTimeInMillis;
    private long id;
    private AlarmRealm newAlarmRealm;
    private Context context;


    public AlarmStartedService() {
        super("AlarmStartedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        //while (true) {


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final RealmResults<AlarmRealm> alarmsTurnedOnResults = Realm.getDefaultInstance().where(AlarmRealm.class).equalTo("isOn", true).findAll();

            /*Dla każego aktywnego alaramu z bazy sprawdzenie,
             czy juz nadszedł czas wywołania*/


            for (AlarmRealm alarmRealm : alarmsTurnedOnResults) {
                Log.d("ToLocationId serv1: ", alarmRealm.getToLocationId().toString());
                Log.d("fromLocationId serv1: ", alarmRealm.getFromLocationId().toString());
                //long alarmTimeInMillis = alarmRealm.getAlarmTimeInMillis();
                long curentSystemTimeInMillis = System.currentTimeMillis();
                //long alarmTimeInMillis = curentSystemTimeInMillis + 5000;
                long alarmTimeInMillis = alarmRealm.getAlarmTimeInMillis();

                //zmienic status alarmu na nieaktywny???
                if (alarmTimeInMillis >= curentSystemTimeInMillis) {
                    startAlarmActivity();
                    //break;
                }
            }

            /*
            Dla każego aktywnego alaramu z bazy
              pobranie bieżacego czasu podrózyserv1
              i zapisanie transakcja do bazy danych*/

            for (final AlarmRealm oldAlarmRealm : alarmsTurnedOnResults) {

                final Realm defaultInstance = Realm.getDefaultInstance();

//                Log.d("fromLocationId 1:", fromLocationId);
                getRouteDetailsRequest = new GetRouteDetailsRequest(
                        this,
                        oldAlarmRealm.getFromLocationId(),
                        oldAlarmRealm.getToLocationId(),
                        oldAlarmRealm.getTransportMode()
                );

                getRouteDetailsRequest.setOnResponseListener(new OnResponseListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        String stringRoutePoints = Parser.parseRoutePoints(response);
                        if (!stringRoutePoints.isEmpty()) {
                            id = oldAlarmRealm.getId();

                            responsePoints = GoogleDirectionsHelper.decodePoly(stringRoutePoints);//= Parser.parseDirections(response);
                            routeTime = Parser.parseWholeRouteTime(response);
                            routeTimeInSeconds = Parser.parseRouteTimeInSekonds(response);

                            oldAlarmTimeInMillis = oldAlarmRealm.getAlarmTimeInMillis();
                            oldTravelTimeInmillis = oldAlarmRealm.getRouteTimeInSeconds() * 1000;
                            newTravelTimeInMillis = routeTimeInSeconds * 1000;
                            newAlarmTimeInMillis = oldAlarmTimeInMillis - oldTravelTimeInmillis + newTravelTimeInMillis;

                            newAlarmRealm = new AlarmRealm();

                            /*Te pola są zmieniane*/
                            newAlarmRealm.setId(oldAlarmRealm.getId());
                            newAlarmRealm.setAlarmTimeInMillis(newAlarmTimeInMillis);
                            newAlarmRealm.setRouteTimeLabel(routeTime);

                            /*Te pola sa przepisywane bez zmian*/
                            newAlarmRealm.setId(oldAlarmRealm.getId());
                            newAlarmRealm.setAlarmHour(oldAlarmRealm.getAlarmHour());
                            newAlarmRealm.setAlarmMinute(oldAlarmRealm.getAlarmMinute());
                            newAlarmRealm.setAmPm(oldAlarmRealm.getAmPm());
                            newAlarmRealm.setAlarmDayOfWeek(oldAlarmRealm.getAlarmDayOfWeek());
                            newAlarmRealm.setAlarmDayOfWeekAsInt(oldAlarmRealm.getAlarmDayOfWeekAsInt());
                            newAlarmRealm.setStartPoint(oldAlarmRealm.getStartPoint());
                            newAlarmRealm.setDestinationPoint(oldAlarmRealm.getDestinationPoint());
                            newAlarmRealm.setRouteTimeInSeconds(oldAlarmRealm.getRouteTimeInSeconds());
                            newAlarmRealm.setPreparingTimeInMins(oldAlarmRealm.getPreparingTimeInMins());
                            newAlarmRealm.setLngLatPointsRealmList(oldAlarmRealm.getLngLatPointsRealmList());
                            newAlarmRealm.setAlarmDateTimeData(oldAlarmRealm.getAlarmDateTimeData());
                            newAlarmRealm.setTransportMode(oldAlarmRealm.getTransportMode());
                            newAlarmRealm.setGoalHourOfDay(oldAlarmRealm.getGoalHourOfDay());
                            newAlarmRealm.setGoalMinute(oldAlarmRealm.getGoalMinute());
                            newAlarmRealm.setFromLocationId(oldAlarmRealm.getFromLocationId());
                            newAlarmRealm.setToLocationId(oldAlarmRealm.getToLocationId());

                            //cd przepisanie pol
                            defaultInstance.beginTransaction();
                            defaultInstance.copyToRealmOrUpdate(newAlarmRealm);
                            defaultInstance.commitTransaction();

                        }
                    }

                    @Override
                    public void onFailure() {
                        Log.d("INTERFEJS", "ERROR");
                    }
                });
                getRouteDetailsRequest.execute();
            }
        //}
    }

    private void startAlarmActivity() {
        //Właczenie ALarmActivity
        Intent alarmActivityIntent = new Intent(AlarmStartedService.this, AlarmActivity.class);
        alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(alarmActivityIntent);

    }

//    private void getRouteTime() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                setRequest();
//                Toast.makeText(getApplicationContext(), routeTime, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void setRequest() {
//        getRouteDetailsRequest = new GetRouteDetailsRequest(this, fromLocationId, toLocationId, GoogleTransportMode.bicycling);
//        getRouteDetailsRequest.setOnResponseListener(new OnResponseListener() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                Log.d("AlarmStartedService", "onSuccess");
//                responsePoints = GoogleDirectionsHelper.decodePoly(Parser.parseRoutePoints(response));//= Parser.parseDirections(response);
//                routeTime = Parser.parseWholeRouteTime(response);
//                Log.d("AlarmStartedService", "czas przejazdu " + routeTime);
//                routeTimeInSeconds = Parser.parseRouteTimeInSekonds(response);
//            }
//
//            @Override
//            public void onFailure() {
//                Log.d("AlarmStartedService", "bład requesta w setRequest()");
//            }
//        });
//        getRouteDetailsRequest.execute();
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        public AlarmStartedService getService() {
            return AlarmStartedService.this;
        }
    }
}