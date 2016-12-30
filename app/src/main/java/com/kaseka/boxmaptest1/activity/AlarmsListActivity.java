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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms_list);
        getSupportActionBar().hide();

        bAddAlarm = (Button)findViewById(R.id.bAddAlarm);
        bAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMainActivityIntent = new Intent(context, MainActivity.class);
                context.startActivity(startMainActivityIntent);
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

    public void deleteAlarm(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Delete alarm?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void setToast(View v){
        Toast.makeText(v.getContext(), "CloseAlarm ib", Toast.LENGTH_SHORT).show();
    }

}
