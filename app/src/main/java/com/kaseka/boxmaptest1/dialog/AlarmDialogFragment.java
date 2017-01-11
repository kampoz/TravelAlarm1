package com.kaseka.boxmaptest1.dialog;


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

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.activity.AlarmsListActivity;
import com.kaseka.boxmaptest1.activity.MainActivity;
import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;
import com.kaseka.boxmaptest1.helper.Cache;
import com.kaseka.boxmaptest1.helper.MyDisplayTimeHelper;
import com.kaseka.boxmaptest1.service.AlarmActivateStartedService;

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
        tvAlarmHour.setText(MyDisplayTimeHelper.setDisplayTime(String.valueOf(alarmPOJO.getAlarmHour()),String.valueOf(alarmPOJO.getAlarmMinute())));
        tvDialogWeekDay.setText("Alarm day: "+alarmPOJO.getAlarmDayOfWeek());
        tvDialogStartPoint.setText("from: "+alarmPOJO.getStartPoint());
        tvDialogDestinationPoint.setText("to: "+alarmPOJO.getDestinationPoint());
        tvDialogTransportMode.setText("transport: "+alarmPOJO.getTransportMode());
        tvDialogGoalTime.setText("arrive time: "+alarmPOJO.getGoalHourOfDay()+" : "+alarmPOJO.getGoalMinute());
        tvDialogGoalTime.setText("arrive time: "+MyDisplayTimeHelper
                .setDisplayTime(String.valueOf(alarmPOJO.getGoalHourOfDay()),String.valueOf(alarmPOJO.getGoalMinute())));
        tvDialogPreparingTime.setText("preparig time: "+alarmPOJO.getPreparingTimeInMins()+" mins");
        tvDialogTravelTime.setText("travel time: "+alarmPOJO.getRouteTimeLabel());


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
