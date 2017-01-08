package com.kaseka.boxmaptest1.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.adapter.AlarmsListViewAdapter;
import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;
import com.kaseka.boxmaptest1.data.realm.AlarmRealm;
import com.kaseka.boxmaptest1.dialog.AlarmDialogFragment;
import com.kaseka.boxmaptest1.helper.GoogleDirectionsHelper;
import com.kaseka.boxmaptest1.helper.Parser;
import com.kaseka.boxmaptest1.listener.OnResponseListener;
import com.kaseka.boxmaptest1.networking.GetRouteDetailsRequest;
import com.mapbox.mapboxsdk.geometry.LatLng;

import android.app.AlertDialog;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static android.R.attr.button;

public class AlarmsListActivity extends AppCompatActivity {

    Button bAddAlarm;
    Context context = AlarmsListActivity.this;
    private Toolbar toolbar;
    GetRouteDetailsRequest getRouteDetailsRequest;
    private AlarmRealm newAlarmRealm;
    private ArrayList<LatLng> responsePoints = new ArrayList<>();
    String routeTime;
    int routeTimeInSeconds = 0;
    private long id;
    private long oldAlarmTimeInMillis;
    private long oldTravelTimeInmillis;
    private long newTravelTimeInMillis;
    private long newAlarmTimeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms_list);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
        //getSupportActionBar().hide();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Alarms");
        setSupportActionBar(toolbar);

        bAddAlarm = (Button)findViewById(R.id.bAddAlarm);
        //bAddAlarm.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_button_gray_pressed));
        bAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMainActivityIntent = new Intent(context, MainActivity.class);
                context.startActivity(startMainActivityIntent);
                finish();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.alarmsRecyclerView);
        // w celach optymalizacji
        recyclerView.setHasFixedSize(true);

        // ustawiamy LayoutManagera
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
        widok gridview
        */
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        // ustawiamy animatora, który odpowiada za animację dodania/usunięcia elementów listy
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // tworzymy źródło danych - tablicę z artykułami
        ArrayList<AlarmRealm> alarms = new ArrayList<>();

        //setTestRealmDataBase();

        alarms.addAll(
                Realm.getDefaultInstance().where(AlarmRealm.class).findAll()
        );
        // tworzymy adapter oraz łączymy go z RecyclerView
        recyclerView.setAdapter(new AlarmsListViewAdapter(alarms, recyclerView));

         new RealmActualizationAsyncTask(getApplicationContext()).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_add_alarm){
            Intent startMainActivityIntent = new Intent(context, MainActivity.class);
            context.startActivity(startMainActivityIntent);
            finish();
        }

        if (id==R.id.action_show_alarms_list){

        }

        if (id==R.id.action_setting){

        }

        if (id==R.id.action_about){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AlarmsListActivity.this);
            alertDialogBuilder.setMessage("Copyright \u00a9 2017\nKamil Poznakowski\nkampoznak@gmail.com");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }



    public void setToast(View v){
        Toast.makeText(v.getContext(), "CloseAlarm ib", Toast.LENGTH_SHORT).show();
    }


    private class RealmActualizationAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;

        public RealmActualizationAsyncTask (Context context){
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //final Realm realm = Realm.getInstance(config);
//            RealmConfiguration config = new RealmConfiguration
//                    .Builder()
//                    .deleteRealmIfMigrationNeeded()
//                    .build();
//
//
//            Realm realm  = null;
            Realm realm  = Realm.getDefaultInstance();
            RealmResults<AlarmRealm> alarmsTurnedOnResults = realm.where(AlarmRealm.class).equalTo("isOn", true).findAll();



            /*
            Dla każego aktywnego alaramu z bazy
              pobranie bieżacego czasu podrózyserv1
              i zapisanie transakcja do bazy danych*/

            for (final AlarmRealm oldAlarmRealm : alarmsTurnedOnResults) {
                final long id = oldAlarmRealm.getId();
//                Log.d("fromLocationId 1:", fromLocationId);
                getRouteDetailsRequest = new GetRouteDetailsRequest(
                        AlarmsListActivity.this,
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

                        Realm realm  = null;
                        realm  = Realm.getInstance(config);

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
                    //startAlarmActivity();
                    //break;
                }
            }

            return null;


        }
    }

}
