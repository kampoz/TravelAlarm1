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
import com.kaseka.boxmaptest1.helper.MyDisplayTimeHelper;

public class AlarmOnOffFragment extends DialogFragment{

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




        //tvAlarmHour.setText(AlarmPOJO.getAlarmHour()+" : "+AlarmPOJO.getAlarmMinute());
        tvAlarmHour.setText(MyDisplayTimeHelper.setDisplayTime(String.valueOf(AlarmPOJO.getAlarmHour()),String.valueOf(AlarmPOJO.getAlarmMinute())));
        tvDialogWeekDay.setText("Alarm day: "+AlarmPOJO.getAlarmDayOfWeek());
        tvDialogStartPoint.setText("z: "+AlarmPOJO.getStartPoint());
        tvDialogDestinationPoint.setText("do: "+AlarmPOJO.getDestinationPoint());
        tvDialogTransportMode.setText("transport: "+AlarmPOJO.getTransportMode());
        tvDialogGoalTime.setText("arrive time: "+AlarmPOJO.getGoalHourOfDay()+" : "+AlarmPOJO.getGoalMinute());
        tvDialogGoalTime.setText("arrive time: "+MyDisplayTimeHelper
                .setDisplayTime(String.valueOf(AlarmPOJO.getGoalHourOfDay()),String.valueOf(AlarmPOJO.getGoalMinute())));
        tvDialogPreparingTime.setText("preparig time: "+AlarmPOJO.getPreparingTimeInMins()+" mins");
        tvDialogTravelTime.setText("travel time: "+AlarmPOJO.getRouteTimeLabel());
        isOn = AlarmPOJO.getIsOn();

        String onOffButtonLabel = "";
        if(isOn){
            onOffButtonLabel = "OFF";
        }else onOffButtonLabel = "ON";

        builder.setView(view);
        builder.setNegativeButton(onOffButtonLabel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if(isOn){
//                    isOn=false;
//                    AlarmPOJO.setIsOn(false);
//                    AlarmPOJO.setIsOntoRealm();
//
//                }else {
//                    isOn=true;
//                    AlarmPOJO.setIsOn(false);
//                }
//                AlarmPOJO.setIsOntoRealm();
            }
        });

//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                AlarmPOJO.insertAlarmToRealm();
//                Intent startAlarmListActivityIntent = new Intent(getActivity(), AlarmsListActivity.class);
//                getActivity().startActivity(startAlarmListActivityIntent);
//            }
//        });

//        bDeleteAlarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "CloseAlarm ib", Toast.LENGTH_SHORT).show();
//            }
//        });


        Dialog dialog = builder.create();
        return dialog;



    }


}
