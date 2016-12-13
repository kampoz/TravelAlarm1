package com.kaseka.boxmaptest1.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.adapter.MyAdapter;
import com.kaseka.boxmaptest1.data.realm.AlarmRealm;

import java.util.ArrayList;

import io.realm.Realm;

public class AlarmsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms_list);
        getSupportActionBar().hide();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.alarmsRecyclerView);
        // w celach optymalizacji
        recyclerView.setHasFixedSize(true);

        // ustawiamy LayoutManagera
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ustawiamy animatora, który odpowiada za animację dodania/usunięcia elementów listy
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        // tworzymy źródło danych - tablicę z artykułami
        ArrayList<AlarmRealm> alarms = new ArrayList<>();

        setTestRealmDataBase();

        alarms.addAll(
                Realm.getDefaultInstance().where(AlarmRealm.class).findAll()
        );
        // tworzymy adapter oraz łączymy go z RecyclerView
        recyclerView.setAdapter(new MyAdapter(alarms, recyclerView));
    }

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
}
