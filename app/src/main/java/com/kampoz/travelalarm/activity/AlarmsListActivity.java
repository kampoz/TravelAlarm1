package com.kampoz.travelalarm.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.kampoz.travelalarm.R;
import com.kampoz.travelalarm.adapter.AlarmsListViewAdapter;
import com.kampoz.travelalarm.data.realm.AlarmRealm;
import com.kampoz.travelalarm.networking.GetRouteDetailsRequest;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

import io.realm.Realm;

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

    private AlarmsListViewAdapter alarmsListViewAdapter;
    ArrayList<AlarmRealm> alarms = new ArrayList<>();

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


        //setTestRealmDataBase();

        alarms.clear();
        alarms.addAll(
                Realm.getDefaultInstance().where(AlarmRealm.class).findAll()
        );
        alarmsListViewAdapter = new AlarmsListViewAdapter(alarms, recyclerView);
        // tworzymy adapter oraz łączymy go z RecyclerView
        recyclerView.setAdapter(alarmsListViewAdapter);

        //setFirstAlarmInRealm();

       // new RealmActualizationAsyncTask(getApplicationContext()).execute();


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
            Intent startMainActivityIntent = new Intent(this, MainActivity.class);
            this.startActivity(startMainActivityIntent);
            finish();
        }

        if (id==R.id.action_show_alarms_list){

        }

        if (id==R.id.action_setting){
            Intent startSettingsActivityIntent = new Intent(this, SettingsActivity.class);
            this.startActivity(startSettingsActivityIntent);
            this.finish();

        }

        if (id==R.id.action_about){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AlarmsListActivity.this);
            alertDialogBuilder.setMessage(R.string.author_data);
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

    @Override
    public void onResume(){
        super.onResume();
        alarms.clear();
        alarms.addAll(
                Realm.getDefaultInstance().where(AlarmRealm.class).findAll()
        );
        alarmsListViewAdapter.notifyDataSetChanged();
    }

}
