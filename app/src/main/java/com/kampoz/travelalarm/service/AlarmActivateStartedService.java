package com.kampoz.travelalarm.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.kampoz.travelalarm.activity.AlarmActivity;
import com.kampoz.travelalarm.activity.AlarmsListActivity;
import com.kampoz.travelalarm.data.realm.AlarmRealm;
import com.kampoz.travelalarm.helper.GoogleDirectionsHelper;
import com.kampoz.travelalarm.helper.Parser;
import com.kampoz.travelalarm.listener.OnResponseListener;
import com.kampoz.travelalarm.networking.GetRouteDetailsRequest;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AlarmActivateStartedService extends IntentService {

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
    private AlarmRealm newAlarmRealm;
    private final long MILLIS_IN_ONE_WEEK = 1000 * 60 * 60 * 24 * 7;
    //private final long SLEEP_TIME = 1000*60*15;
    private final long SLEEP_TIME = 1000*60;

    RealmResults<AlarmRealm> alarmsTurnedOnResults;


    public AlarmActivateStartedService() {
        super("AlarmActivateStartedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        while (true) {
            Realm realm = Realm.getDefaultInstance();
            alarmsTurnedOnResults = realm.where(AlarmRealm.class).equalTo("isOn", true).findAll();
            int size = alarmsTurnedOnResults.size();
            Log.d("size = ", String.valueOf(size));
            //updateAlarmsTravelTimesFromGoogle(getActiveAlarms());
            //checkingIfAlarmsShouldBeActivate(alarmsTurnedOnResults);


            for (final AlarmRealm alarmRealm : alarmsTurnedOnResults) {
//            Log.d("ToLocationId serv1: ", alarmRealm.getToLocationId().toString());
//            Log.d("fromLocationId serv1: ", alarmRealm.getFromLocationId().toString());

                //long alarmTimeInMillis = alarmRealm.getAlarmTimeInMillis();
                long curentSystemTimeInMillis = System.currentTimeMillis();
                //final long alarmTimeInMillis = curentSystemTimeInMillis + 5000;
                final long alarmTimeInMillis = alarmRealm.getAlarmTimeInMillis();

                long sum = alarmTimeInMillis - curentSystemTimeInMillis;
                Log.d("SUM: ", "curentTimeInMillis: " + (curentSystemTimeInMillis));
                Log.d("SUM: ", "alarmTimeInMillis+ " + String.valueOf(alarmTimeInMillis));
                Log.d("SUM: ", String.valueOf(sum));

                //zmienic status alarmu na nieaktywny???
                // uruchomienie alarmu

                if (curentSystemTimeInMillis >= alarmTimeInMillis) {

                    startAlarmActivity(alarmRealm);

//                final AlarmRealm alarmRealmToChange = new AlarmRealm();
//                alarmRealmToChange.setId(alarmRealm.getId());
//                alarmRealmToChange.setAlarmTimeInMillis(alarmTimeInMillis + MILLIS_IN_ONE_WEEK);

                    //Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            alarmRealm.setAlarmTimeInMillis(alarmTimeInMillis + MILLIS_IN_ONE_WEEK);
                            alarmRealm.setIsOn(false);
                            alarmRealm.setAlarmDateTimeData((new DateTime(alarmTimeInMillis + MILLIS_IN_ONE_WEEK)).toString());

                            //realm.copyToRealmOrUpdate(alarmRealmToChange);
                        }
                    });

                }
            }

            alarmsTurnedOnResults = null;

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            realm.close();
        }

    }


    private RealmResults<AlarmRealm> getActiveAlarms() {
        Realm realm = null;
        realm = Realm.getDefaultInstance();
        RealmResults<AlarmRealm> alarmsTurnedOnResults = realm.where(AlarmRealm.class).equalTo("isOn", true).findAll();
        realm = null;
        return alarmsTurnedOnResults;
    }

    private void updateAlarmsTravelTimesFromGoogle(RealmResults<AlarmRealm> alarmsTurnedOnResults) {
        for (final AlarmRealm oldAlarmRealm : alarmsTurnedOnResults) {
            final long id = oldAlarmRealm.getId();
//                Log.d("fromLocationId 1:", fromLocationId);
            getRouteDetailsRequest = new GetRouteDetailsRequest(
                    AlarmActivateStartedService.this,
                    oldAlarmRealm.getFromLocationId(),
                    oldAlarmRealm.getToLocationId(),
                    oldAlarmRealm.getTransportMode()
            );

            getRouteDetailsRequest.setOnResponseListener(new OnResponseListener() {
                @Override
                public void onSuccess(JSONObject response) {

                    RealmConfiguration config = new RealmConfiguration
                            .Builder()
                            //.deleteRealmIfMigrationNeeded()
                            .build();

                    Realm realm = Realm.getInstance(config);
                    AlarmRealm currentAlarmRealm = realm.where(AlarmRealm.class).equalTo("id", id).findFirst();

                    Log.d("currentAlarmRealm", String.valueOf(currentAlarmRealm.getId()));

                    //realm.where(AlarmRealm.class).equalTo("id", id).findFirst();

                    String stringRoutePoints = Parser.parseRoutePoints(response);
                    if (!stringRoutePoints.isEmpty()) {
                        responsePoints = GoogleDirectionsHelper.decodePoly(stringRoutePoints);//= Parser.parseDirections(response);
                        routeTime = Parser.parseWholeRouteTime(response);
                        routeTimeInSeconds = Parser.parseRouteTimeInSekonds(response);

                        oldAlarmTimeInMillis = currentAlarmRealm.getAlarmTimeInMillis();
                        oldTravelTimeInmillis = currentAlarmRealm.getRouteTimeInSeconds() * 1000;
                        newTravelTimeInMillis = routeTimeInSeconds * 1000;
                        newAlarmTimeInMillis = oldAlarmTimeInMillis - oldTravelTimeInmillis + newTravelTimeInMillis;

                        final AlarmRealm newAlarmRealm = new AlarmRealm();

                            /*Te pola są zmieniane*/
                        newAlarmRealm.setId(currentAlarmRealm.getId());
                        newAlarmRealm.setAlarmTimeInMillis(newAlarmTimeInMillis);
                        newAlarmRealm.setRouteTimeLabel(routeTime);

                            /*Te pola sa przepisywane bez zmian*/
                        newAlarmRealm.setIsOn(currentAlarmRealm.getIsOn());
                        newAlarmRealm.setAlarmHour(currentAlarmRealm.getAlarmHour());
                        newAlarmRealm.setAlarmMinute(currentAlarmRealm.getAlarmMinute());
                        newAlarmRealm.setAmPm(currentAlarmRealm.getAmPm());
                        newAlarmRealm.setAlarmDayOfWeek(currentAlarmRealm.getAlarmDayOfWeek());
                        newAlarmRealm.setAlarmDayOfWeekAsInt(currentAlarmRealm.getAlarmDayOfWeekAsInt());
                        newAlarmRealm.setStartPoint(currentAlarmRealm.getStartPoint());
                        newAlarmRealm.setDestinationPoint(currentAlarmRealm.getDestinationPoint());
                        newAlarmRealm.setRouteTimeInSeconds(currentAlarmRealm.getRouteTimeInSeconds());
                        newAlarmRealm.setPreparingTimeInMins(currentAlarmRealm.getPreparingTimeInMins());
                        newAlarmRealm.setLngLatPointsRealmList(currentAlarmRealm.getLngLatPointsRealmList());
                        newAlarmRealm.setAlarmDateTimeData(currentAlarmRealm.getAlarmDateTimeData());
                        newAlarmRealm.setTransportMode(currentAlarmRealm.getTransportMode());
                        newAlarmRealm.setGoalHourOfDay(currentAlarmRealm.getGoalHourOfDay());
                        newAlarmRealm.setGoalMinute(currentAlarmRealm.getGoalMinute());
                        newAlarmRealm.setFromLocationId(currentAlarmRealm.getFromLocationId());
                        newAlarmRealm.setToLocationId(currentAlarmRealm.getToLocationId());

                        Log.d("newAlarmRealm: ", newAlarmRealm.getStartPoint());
                        Log.d("newAlarmRealm: ", newAlarmRealm.getDestinationPoint());
                        Log.d("newAlarmRealm: ", newAlarmRealm.getRouteTimeLabel());

                        //cd przepisanie pol
//                            realm.beginTransaction();
//                            realm.copyToRealmOrUpdate(newAlarmRealm);
//                            realm.commitTransaction();

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(newAlarmRealm);
                            }
                        });
                    }
                }

                @Override
                public void onFailure() {
                    Log.d("Request error: ", "ERROR");
                }
            });
            getRouteDetailsRequest.execute();
        }

    }

    /*Dla każego aktywnego alaramu z bazy sprawdzenie,
     czy juz nadszedł czas wywołania, jesli tak, to wykonuje
     i nadpisuje czas wykonania w millis na za tydzień*/
    private void checkingIfAlarmsShouldBeActivate(RealmResults<AlarmRealm> alarmsTurnedOnResults) {
        for (final AlarmRealm alarmRealm : alarmsTurnedOnResults) {
//            Log.d("ToLocationId serv1: ", alarmRealm.getToLocationId().toString());
//            Log.d("fromLocationId serv1: ", alarmRealm.getFromLocationId().toString());

            //long alarmTimeInMillis = alarmRealm.getAlarmTimeInMillis();
            long curentSystemTimeInMillis = System.currentTimeMillis();
            //long alarmTimeInMillis = curentSystemTimeInMillis + 5000;
            final long alarmTimeInMillis = alarmRealm.getAlarmTimeInMillis();

            //zmienic status alarmu na nieaktywny???
            // uruchomienie alarmu
            if (alarmTimeInMillis >= curentSystemTimeInMillis) {
                startAlarmActivity(alarmRealm);

//                final AlarmRealm alarmRealmToChange = new AlarmRealm();
//                alarmRealmToChange.setId(alarmRealm.getId());
//                alarmRealmToChange.setAlarmTimeInMillis(alarmTimeInMillis + MILLIS_IN_ONE_WEEK);

                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        alarmRealm.setAlarmTimeInMillis(alarmTimeInMillis + MILLIS_IN_ONE_WEEK);
                        //realm.copyToRealmOrUpdate(alarmRealmToChange);
                    }
                });
            }
        }
    }


    private void startAlarmActivity(AlarmRealm alarmRealm) {
        //Właczenie ALarmActivity
        Intent alarmActivityIntent = new Intent(AlarmActivateStartedService.this, AlarmActivity.class);
        alarmActivityIntent.putExtra("ALARM_DAY_OF_WEEK", alarmRealm.getAlarmDayOfWeek());
        alarmActivityIntent.putExtra("DESTINATION", alarmRealm.getDestinationPoint());
        alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(alarmActivityIntent);

        /*zmiana czasu wywołania alarmu; dodanie tygodnia w milisekundach*/
    }

    private void startAlarmsListActivity() {
        //Właczenie ALarmActivity
        Intent alarmsListActivityIntent = new Intent(AlarmActivateStartedService.this, AlarmsListActivity.class);
        alarmsListActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(alarmsListActivityIntent);

        /*zmiana czasu wywołania alarmu; dodanie tygodnia w milisekundach*/
    }


//
//    private void setRequest() {
//        getRouteDetailsRequest = new GetRouteDetailsRequest(this, fromLocationId, toLocationId, GoogleTransportMode.bicycling);
//        getRouteDetailsRequest.setOnResponseListener(new OnResponseListener() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                Log.d("AlarmActivateStartedService", "onSuccess");
//                responsePoints = GoogleDirectionsHelper.decodePoly(Parser.parseRoutePoints(response));//= Parser.parseDirections(response);
//                routeTime = Parser.parseWholeRouteTime(response);
//                Log.d("AlarmActivateStartedService", "czas przejazdu " + routeTime);
//                routeTimeInSeconds = Parser.parseRouteTimeInSekonds(response);
//            }
//
//            @Override
//            public void onFailure() {
//                Log.d("AlarmActivateStartedService", "bład requesta w setRequest()");
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
        public AlarmActivateStartedService getService() {
            return AlarmActivateStartedService.this;
        }
    }
}