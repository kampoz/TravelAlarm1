package com.kaseka.boxmaptest1.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.activity.AlarmsListActivity;
import com.kaseka.boxmaptest1.data.realm.AlarmRealm;

import com.kaseka.boxmaptest1.dialog.AlarmInfoFragment;
import com.kaseka.boxmaptest1.helper.Cache;
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
        public ImageButton ibDeleteAlarm;
        private ImageButton ibGreenLight;

        public MyViewHolder(View view) {
            super(view);

            tvAlarmHour = (TextView) view.findViewById(R.id.tvAlarmMhour);
            tvAlarmDay = (TextView) view.findViewById(R.id.tvAlarmDay);
            ibDeleteAlarm = (ImageButton) view.findViewById(R.id.ibDeleteAlarm);
            ibGreenLight = (ImageButton)view.findViewById(R.id.ibGreenLight);
            //lLsingleAlarm = (LinearLayout)view.findViewById(R.id.lLsingleAlarm);




//           tvDestinaitonPoint = (TextView) view.findViewById(R.id.tvDestinationPoint);
//           tvDestinaitonHour = (TextView) view.findViewById(R.id.tvDestinationHour);
//           bEdit = (Button) view.findViewById(R.id.bEdit);
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

//                AlarmPOJO.setAlarmPOJODataFromAlarmRealm(alarmRealmPosition);

                Cache.clearAlarmPOJO().setAlarmPOJODataFromAlarmRealm(alarmRealmPosition);

//                FragmentManager manager = ((AlarmsListActivity) context).getFragmentManager();
//                AlarmDialogFragment myDialog = new AlarmDialogFragment();
//                myDialog.show(manager, "myDialog");

                FragmentManager manager = ((AlarmsListActivity) context).getFragmentManager();
                AlarmInfoFragment myDialog = new AlarmInfoFragment();
                myDialog.show(manager, "myDialog");

                //Toast.makeText(view.getContext(), id+" "+day, Toast.LENGTH_SHORT).show();
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

        if(!alarmRealm.getIsOn()) {
            ((MyViewHolder) viewHolder).ibGreenLight.setBackgroundResource(R.drawable.little_circle_gray_shape);
        }else{
            ((MyViewHolder) viewHolder).ibGreenLight.setBackgroundResource(R.drawable.little_circle_shape);
        }


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

        ((MyViewHolder) viewHolder).ibGreenLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean alarmRealmisOn = alarmRealm.getIsOn();
                //
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(AlarmRealm.class).equalTo("id", id).findFirst().setIsOn(!alarmRealmisOn);
                        Toast.makeText(((MyViewHolder) viewHolder).ibGreenLight.getContext(), "isOn Realm status: "+alarmRealm.getIsOn(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                alarms.clear();
                alarms.addAll(Realm.getDefaultInstance().where(AlarmRealm.class).findAll());
                //notifyItemRemoved(position);
                notifyDataSetChanged();
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