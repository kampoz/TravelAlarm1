package com.kaseka.boxmaptest1.adapter;

import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.activity.AlarmsListActivity;
import com.kaseka.boxmaptest1.activity.MainActivity;
import com.kaseka.boxmaptest1.data.realm.AlarmRealm;
import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;

import com.kaseka.boxmaptest1.dialog.AlarmDialogFragment;
import com.kaseka.boxmaptest1.dialog.AlarmOnOffFragment;
import com.kaseka.boxmaptest1.helper.MyDisplayTimeHelper;

import java.util.ArrayList;

import io.realm.Realm;

public class AlarmsListViewAdapter extends RecyclerView.Adapter {
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
        public long id;
        public TextView tvDestinaitonPoint;
        public TextView tvDestinaitonHour;
        public Button bEdit;
        public ImageButton ibDeleteAlarm;
        private AlarmRealm alarmRealm;
        private LinearLayout lLsingleAlarm;




        public MyViewHolder(View pItem) {
            super(pItem);

            tvAlarmHour = (TextView) pItem.findViewById(R.id.tvAlarmMhour);
            tvAlarmDay = (TextView) pItem.findViewById(R.id.tvAlarmDay);
            ibDeleteAlarm = (ImageButton) pItem.findViewById(R.id.ibDeleteAlarm);
            lLsingleAlarm = (LinearLayout)pItem.findViewById(R.id.lLsingleAlarm);




//           tvDestinaitonPoint = (TextView) pItem.findViewById(R.id.tvDestinationPoint);
//           tvDestinaitonHour = (TextView) pItem.findViewById(R.id.tvDestinationHour);
//           bEdit = (Button) pItem.findViewById(R.id.bEdit);
//            tvAlarmDay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), "tvAlarmDay", Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }

    // konstruktor adaptera
    public AlarmsListViewAdapter(ArrayList<AlarmRealm> mAlarms, RecyclerView pRecyclerView){
        alarms = mAlarms;
        mRecyclerView = pRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        // tworzymy layout itemu oraz obiekt ViewHoldera
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_alarm_layout, viewGroup, false);

        // dla elementu listy ustawiamy obiekt OnClickListener,
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                int clickedPosition = mRecyclerView.getChildAdapterPosition(view);
                AlarmRealm alarmRealmPosition = alarms.get(clickedPosition);

                long id = alarmRealmPosition.getId();
                String day = alarmRealmPosition.getAlarmDayOfWeek();

                AlarmPOJO.setAlarmPOJODataFromAlarmRealm(alarmRealmPosition);

//                FragmentManager manager = ((AlarmsListActivity) context).getFragmentManager();
//                AlarmDialogFragment myDialog = new AlarmDialogFragment();
//                myDialog.show(manager, "myDialog");

                FragmentManager manager = ((AlarmsListActivity) context).getFragmentManager();
                AlarmOnOffFragment myDialog = new AlarmOnOffFragment();
                myDialog.show(manager, "myDialog");

                Toast.makeText(view.getContext(), id+" "+day, Toast.LENGTH_SHORT).show();
                // odnajdujemy indeks klikniętego elementu
                //int positionToDelete = mRecyclerView.getChildAdapterPosition(view);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        // uzupełniamy layout alarmu
        final AlarmRealm alarmRealm = alarms.get(position);


        final long id = alarmRealm.getId();
        final boolean isAlarmOn = alarmRealm.getIsOn();
        ((MyViewHolder) viewHolder).tvAlarmHour.setText(MyDisplayTimeHelper.setDisplayTime(
                String.valueOf(alarmRealm.getAlarmHour()), String.valueOf(alarmRealm.getAlarmMinute())));
        ((MyViewHolder) viewHolder).tvAlarmDay.setText(alarmRealm.getAlarmDayOfWeek());

        if(isAlarmOn) {
            ((MyViewHolder) viewHolder).lLsingleAlarm.setBackgroundResource(R.color.colorMyDarkGreen);
        }


        /*((MyViewHolder) viewHolder).tvAlarmHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                AlarmPOJO.setAlarmPOJODataFromAlarmRealm(alarmRealm);

                FragmentManager manager = ((AlarmsListActivity) context).getFragmentManager();
                AlarmDialogFragment myDialog = new AlarmDialogFragment();
                myDialog.show(manager, "myDialog");
            }
        });*/

        ((MyViewHolder) viewHolder).ibDeleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setMessage("Delete alarm?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                //usuwanie alarmu w Realma
                                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.where(AlarmRealm.class).equalTo("id", id).findFirst().deleteFromRealm();
                                    }
                                });
                                alarms.clear();
                                alarms.addAll(Realm.getDefaultInstance().where(AlarmRealm.class).findAll());
                                //notifyItemRemoved(position);
                                notifyDataSetChanged();

                            }
                        });


                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

//        ((MyViewHolder) viewHolder).tvDestinaitonPoint.setText("to: "+alarmRealm.getDestinationPoint());
//        ((MyViewHolder) viewHolder).tvDestinaitonHour.setText("arrive: "+MyDisplayTimeHelper.setDisplayTime(
//                String.valueOf(alarmRealm.getGoalHourOfDay()), String.valueOf(alarmRealm.getGoalMinute())));
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }
}