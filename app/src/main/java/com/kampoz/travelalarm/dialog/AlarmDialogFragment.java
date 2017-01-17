package com.kampoz.travelalarm.dialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kampoz.travelalarm.R;
import com.kampoz.travelalarm.activity.AlarmsListActivity;
import com.kampoz.travelalarm.activity.MainActivity;
import com.kampoz.travelalarm.data.realm.AlarmPOJO;
import com.kampoz.travelalarm.helper.Cache;
import com.kampoz.travelalarm.helper.MyDisplayTimeHelper;
import com.kampoz.travelalarm.service.AlarmActivateStartedService;

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

        AlarmPOJO alarmPOJO = Cache.getAlarmPOJO();

        //tvAlarmHour.setText(AlarmPOJO.getAlarmHour()+" : "+AlarmPOJO.getAlarmMinute());
        tvAlarmHour.setText("ALARM TIME "+ MyDisplayTimeHelper.setDisplayTime(String.valueOf(alarmPOJO.getAlarmHour()),String.valueOf(alarmPOJO.getAlarmMinute())));
        tvDialogWeekDay.setText("Alarm day: "+alarmPOJO.getAlarmDayOfWeek());
        tvDialogStartPoint.setText("From: "+alarmPOJO.getStartPoint());
        tvDialogDestinationPoint.setText("To: "+alarmPOJO.getDestinationPoint());
        tvDialogTransportMode.setText("Transport: "+alarmPOJO.getTransportMode());
        //tvDialogGoalTime.setText("arrive time: "+alarmPOJO.getGoalHourOfDay()+" : "+alarmPOJO.getGoalMinute());
        tvDialogGoalTime.setText("Time of arrival: "+MyDisplayTimeHelper
                .setDisplayTime(String.valueOf(alarmPOJO.getGoalHourOfDay()),String.valueOf(alarmPOJO.getGoalMinute())));
        tvDialogPreparingTime.setText("Time to prepare: "+alarmPOJO.getPreparingTimeInMins()+" mins");
        tvDialogTravelTime.setText("Travel time: "+alarmPOJO.getRouteTimeLabel());


        builder.setView(view);
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent startMainActivityIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(startMainActivityIntent);
                //         AlarmDialogFragment.this.startActivity(startMainActivityIntent);
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cache.getAlarmPOJO().insertAlarmToRealm();

                Intent startAlarmListActivityIntent = new Intent(getActivity(), AlarmsListActivity.class);
                getActivity().startActivity(startAlarmListActivityIntent);

                Intent intent = new Intent(getActivity(), AlarmActivateStartedService.class);
                intent.putExtra(AlarmActivateStartedService.EXTRA_MESSAGE, "extra message");

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
