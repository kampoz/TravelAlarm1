package com.kaseka.boxmaptest1.dialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.activity.AlarmsListActivity;
import com.kaseka.boxmaptest1.activity.MainActivity;
import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;
import com.kaseka.boxmaptest1.helper.Cache;
import com.kaseka.boxmaptest1.helper.MyDisplayTimeHelper;

public class AlarmInfoFragment extends DialogFragment{

    boolean isOn = false;

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

        AlarmPOJO alarmPOJO = Cache.getAlarmPOJO();
        //tvAlarmHour.setText(AlarmPOJO.getAlarmHour()+" : "+AlarmPOJO.getAlarmMinute());
        tvAlarmHour.setText("ALARM TIME: "+ MyDisplayTimeHelper.setDisplayTime(String.valueOf(alarmPOJO.getAlarmHour()),String.valueOf(alarmPOJO.getAlarmMinute())));
        tvDialogWeekDay.setText("Alarm day: "+alarmPOJO.getAlarmDayOfWeek());
        tvDialogStartPoint.setText("From: "+alarmPOJO.getStartPoint());
        tvDialogDestinationPoint.setText("To: "+alarmPOJO.getDestinationPoint());
        tvDialogTransportMode.setText("Transport: "+alarmPOJO.getTransportMode());
        //tvDialogGoalTime.setText("arrive time: "+alarmPOJO.getGoalHourOfDay()+" : "+alarmPOJO.getGoalMinute());
        tvDialogGoalTime.setText("Time of arrival: "+MyDisplayTimeHelper
                .setDisplayTime(String.valueOf(alarmPOJO.getGoalHourOfDay()),String.valueOf(alarmPOJO.getGoalMinute())));
        tvDialogPreparingTime.setText("Time to prepare: "+alarmPOJO.getPreparingTimeInMins()+" mins");
        tvDialogTravelTime.setText("Travel time: "+alarmPOJO.getRouteTimeLabel());
        isOn = alarmPOJO.getIsOn();

        builder.setView(view);
        Dialog dialog = builder.create();
        return dialog;
    }

}
