package com.kaseka.boxmaptest1.dialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.activity.AlarmsListActivity;
import com.kaseka.boxmaptest1.activity.ClockFaceActivity;
import com.kaseka.boxmaptest1.activity.MainActivity;
import com.kaseka.boxmaptest1.adapter.AlarmsListViewAdapter;
import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;
import com.kaseka.boxmaptest1.helper.MyDisplayTimeHelper;
import com.kaseka.boxmaptest1.service.TripAlarmStartedService;

public class AlarmDialogFragment extends DialogFragment{

    private Context context;

    @Override
    public Dialog onCreateDialog(Bundle ssvadInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm_dialog, null);

        final TextView tvAlarmHour = (TextView) view.findViewById(R.id.tvDialogAlarmHour);
        final TextView tvDialogWeekDay = (TextView) view.findViewById(R.id.tvDialogWeekDay);
        final TextView tvDialogStartPoint = (TextView) view.findViewById(R.id.tvDialogStartPoint);
        final TextView tvDialogDestinationPoint = (TextView) view.findViewById(R.id.tvDialogDestinationPoint);
        final TextView tvDialogTransportMode = (TextView) view.findViewById(R.id.tvDialogTransportMode);
        final TextView tvDialogGoalTime = (TextView) view.findViewById(R.id.tvDialogGoalTime);
        final TextView tvDialogPreparingTime = (TextView) view.findViewById(R.id.tvDialogPreparingTime);
        final TextView tvDialogTravelTime = (TextView) view.findViewById(R.id.tvDialogTravelTime);
        final ImageButton bDeleteAlarm = (ImageButton) view.findViewById(R.id.ibDeleteAlarm);



        //tvAlarmHour.setText(AlarmPOJO.getAlarmHour()+" : "+AlarmPOJO.getAlarmMinute());
        tvAlarmHour.setText(MyDisplayTimeHelper.setDisplayTime(String.valueOf(AlarmPOJO.getAlarmHour()),String.valueOf(AlarmPOJO.getAlarmMinute())));
        tvDialogWeekDay.setText("Alarm day: "+AlarmPOJO.getAlarmDayOfWeek());
        tvDialogStartPoint.setText("from: "+AlarmPOJO.getStartPoint());
        tvDialogDestinationPoint.setText("to: "+AlarmPOJO.getDestinationPoint());
        tvDialogTransportMode.setText("transport: "+AlarmPOJO.getTransportMode());
        tvDialogGoalTime.setText("arrive time: "+AlarmPOJO.getGoalHourOfDay()+" : "+AlarmPOJO.getGoalMinute());
        tvDialogGoalTime.setText("arrive time: "+MyDisplayTimeHelper
                .setDisplayTime(String.valueOf(AlarmPOJO.getGoalHourOfDay()),String.valueOf(AlarmPOJO.getGoalMinute())));
        tvDialogPreparingTime.setText("preparig time: "+AlarmPOJO.getPreparingTimeInMins()+" mins");
        tvDialogTravelTime.setText("travel time: "+AlarmPOJO.getRouteTimeLabel());


        builder.setView(view);
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlarmPOJO.setToNull();
                Intent startMainActivityIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(startMainActivityIntent);
                //         AlarmDialogFragment.this.startActivity(startMainActivityIntent);
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlarmPOJO.insertAlarmToRealm();
                Intent startAlarmListActivityIntent = new Intent(getActivity(), AlarmsListActivity.class);
                getActivity().startActivity(startAlarmListActivityIntent);

                Intent intent = new Intent(getActivity(), TripAlarmStartedService.class);
                intent.putExtra(TripAlarmStartedService.EXTRA_MESSAGE, "extra message");

                //context.startService(intent);

                getDialog().getContext().startService(intent);
                getActivity().finish();

            }
        });

//        bDeleteAlarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "CloseAlarm ib", Toast.LENGTH_SHORT).show();
//            }
//        });


        Dialog dialog = builder.create();
        return dialog;



    }

    public void setContext(Context context) {
        this.context = context;
    }
}
