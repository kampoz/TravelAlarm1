package com.kaseka.boxmaptest1.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.app.AlertDialog;

import java.util.ArrayList;

import io.realm.Realm;

import static android.R.attr.button;

public class AlarmsListActivity extends AppCompatActivity {

    Button bAddAlarm;
    Context context = AlarmsListActivity.this;
    private Toolbar toolbar;

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

    //tworzenie probnych alarmów do wczytania na liste
    private void setTestRealmDataBase() {
        for (int i = 0; i < 10; ++i) {
            final AlarmRealm alarmRealm = new AlarmRealm();
            alarmRealm.setId(i);
            alarmRealm.setAlarmDayOfWeek(String.valueOf(i));
            alarmRealm.setAlarmHour(15);
            alarmRealm.setDestinationPoint("do Lublina");

            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(alarmRealm);
                }
            });
            Realm.getDefaultInstance().close();
            //alarms.add(alarmRealm);
        }
    }

    public void setToast(View v){
        Toast.makeText(v.getContext(), "CloseAlarm ib", Toast.LENGTH_SHORT).show();
    }

}
