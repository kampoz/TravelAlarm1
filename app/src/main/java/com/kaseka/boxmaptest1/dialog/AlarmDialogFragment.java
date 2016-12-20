package com.kaseka.boxmaptest1.dialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.activity.ClockFaceActivity;
import com.kaseka.boxmaptest1.activity.MainActivity;
import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;

public class AlarmDialogFragment extends DialogFragment{



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

        //tvAlarmHour.setText(AlarmPOJO.getAlarmHour()+" : "+AlarmPOJO.getAlarmMinute());
        tvAlarmHour.setText(ClockFaceActivity.setDisplayTime(String.valueOf(AlarmPOJO.getAlarmHour()),String.valueOf(AlarmPOJO.getAlarmMinute())));
        tvDialogWeekDay.setText("Alarm day: "+AlarmPOJO.getAlarmDayOfWeek());
        tvDialogStartPoint.setText("z: "+AlarmPOJO.getStartPoint());
        tvDialogDestinationPoint.setText("do: "+AlarmPOJO.getDestinationPoint());
        tvDialogTransportMode.setText("transport: "+AlarmPOJO.getTransportMode());
        tvDialogGoalTime.setText("arrive time: "+AlarmPOJO.getGoalHourOfDay()+" : "+AlarmPOJO.getGoalMinute());
        tvDialogPreparingTime.setText("preparig time: "+AlarmPOJO.getPreparingTimeInMins());
        tvDialogTravelTime.setText("travel time: "+AlarmPOJO.getRouteTimeLabel());


        builder.setView(view);
        builder.setNegativeButton("Modyfikuj", new DialogInterface.OnClickListener() {

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

            }
        });



        Dialog dialog = builder.create();
        return dialog;

    }
}
