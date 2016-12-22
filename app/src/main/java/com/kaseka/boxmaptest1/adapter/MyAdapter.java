package com.kaseka.boxmaptest1.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.data.realm.AlarmRealm;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter {

    // źródło danych
    private ArrayList<AlarmRealm> alarms = new ArrayList<>();

    // obiekt listy artykułów
    private RecyclerView mRecyclerView;

    // implementacja wzorca ViewHolder
    // każdy obiekt tej klasy przechowuje odniesienie do layoutu elementu listy
    // dzięki temu wywołujemy findViewById() tylko raz dla każdego elementu
    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAlarmHour;
        public TextView tvAlarmDay;
        public TextView tvDestinaitonPoint;


        public MyViewHolder(View pItem) {
            super(pItem);
            tvAlarmHour = (TextView) pItem.findViewById(R.id.tvAlarmMhour);
            tvAlarmDay = (TextView) pItem.findViewById(R.id.tvAlarmDay);
            tvDestinaitonPoint = (TextView) pItem.findViewById(R.id.tvDestinationPoint);
        }
    }

    // konstruktor adaptera
    public MyAdapter(ArrayList<AlarmRealm> mAlarms, RecyclerView pRecyclerView){
        alarms = mAlarms;
        mRecyclerView = pRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        // tworzymy layout artykułu oraz obiekt ViewHoldera
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_alarm_layout, viewGroup, false);

        // dla elementu listy ustawiamy obiekt OnClickListener,
        // który usunie element z listy po kliknięciu na niego
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // odnajdujemy indeks klikniętego elementu
                //int positionToDelete = mRecyclerView.getChildAdapterPosition(v);
                // usuwamy element ze źródła danych
                //alarms.remove(positionToDelete);
                // poniższa metoda w animowany sposób usunie element z listy
                //notifyItemRemoved(positionToDelete);
            }
        });

        // tworzymy i zwracamy obiekt ViewHolder
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        // uzupełniamy layout alarmu
        AlarmRealm alarmRealm = alarms.get(i);
        ((MyViewHolder) viewHolder).tvAlarmHour.setText(String.valueOf(alarmRealm.getAlarmHour()));
        ((MyViewHolder) viewHolder).tvAlarmDay.setText(alarmRealm.getAlarmDayOfWeek());
        ((MyViewHolder) viewHolder).tvDestinaitonPoint.setText(alarmRealm.getDestinationPoint());
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }
}