package com.kaseka.boxmaptest1.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.kaseka.boxmaptest1.data.realm.AlarmRealm;
import com.kaseka.boxmaptest1.helper.GoogleDirectionsHelper;
import com.kaseka.boxmaptest1.helper.Parser;
import com.kaseka.boxmaptest1.listener.OnResponseListener;
import com.kaseka.boxmaptest1.networking.GetRouteDetailsRequest;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AlarmsUpdateService extends IntentService {

    private Handler handler;
    GetRouteDetailsRequest getRouteDetailsRequest;

    String routeTime;
    int routeTimeInSeconds = 0;

    private long oldAlarmTimeInMillis;
    private long oldTravelTimeInmillis;
    private long newTravelTimeInMillis;
    private long newAlarmTimeInMillis;
    private ArrayList<LatLng> responsePoints = new ArrayList<>();
    private final long SLEEP_TIME = 1000*60*15;


    public AlarmsUpdateService() {
        super("AlarmsUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        while (true) {

            Realm realm = Realm.getDefaultInstance();
            RealmResults<AlarmRealm> alarmsTurnedOnResults = realm.where(AlarmRealm.class).equalTo("isOn", true).findAll();

            for (final AlarmRealm oldAlarmRealm : alarmsTurnedOnResults) {
                final long id = oldAlarmRealm.getId();
//                Log.d("fromLocationId 1:", fromLocationId);
                getRouteDetailsRequest = new GetRouteDetailsRequest(
                        AlarmsUpdateService.this,
                        oldAlarmRealm.getFromLocationId(),
                        oldAlarmRealm.getToLocationId(),
                        oldAlarmRealm.getTransportMode()
                );

                getRouteDetailsRequest.setOnResponseListener(new OnResponseListener() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        RealmConfiguration config = new RealmConfiguration
                                .Builder()
                                .deleteRealmIfMigrationNeeded()
                                .build();

                        Realm realm = Realm.getInstance(config);
                        final AlarmRealm currentAlarmRealm = realm.where(AlarmRealm.class).equalTo("id", id).findFirst();

                        String stringRoutePoints = Parser.parseRoutePoints(response);
                        if (!stringRoutePoints.isEmpty()) {
                            responsePoints = GoogleDirectionsHelper.decodePoly(stringRoutePoints);//= Parser.parseDirections(response);
                            routeTime = Parser.parseWholeRouteTime(response);
                            routeTimeInSeconds = Parser.parseRouteTimeInSekonds(response);

                            oldAlarmTimeInMillis = currentAlarmRealm.getAlarmTimeInMillis();
                            oldTravelTimeInmillis = currentAlarmRealm.getRouteTimeInSeconds() * 1000;
                            newTravelTimeInMillis = routeTimeInSeconds * 1000;
                            newAlarmTimeInMillis = oldAlarmTimeInMillis - oldTravelTimeInmillis + newTravelTimeInMillis;

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    currentAlarmRealm.setAlarmTimeInMillis(newAlarmTimeInMillis);
                                    currentAlarmRealm.setRouteTimeLabel(routeTime);
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

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            realm.close();
        }
    }

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
        public AlarmsUpdateService getService() {
            return AlarmsUpdateService.this;
        }
    }
}
