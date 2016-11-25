package com.kaseka.boxmaptest1;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import net.danlew.android.joda.JodaTimeAndroid;


public class ClockFaceActivity extends AppCompatActivity {

    private TimePicker timePickerSetArriveTime;
    private Calendar calendar;
    private String format = "";
    private TextView ivHourDisplay;
    private Button bSetAlarm;
    ClockView clockView;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private EditText etPreparingTimeInMins;
    private Switch switchAmPm;
    int travelTimeInSeconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_clock_face);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        travelTimeInSeconds = bundle.getInt("travelTimeInSeconds");

        clockView = (ClockView)findViewById(R.id.rlClockParent);
        ivHourDisplay = (TextView)findViewById(R.id.tvHourDisplay);
        etPreparingTimeInMins = (EditText)findViewById(R.id.etPreparingTimeInMins);
        bSetAlarm = (Button) findViewById(R.id.bSetAlarm);
        etPreparingTimeInMins.setText("30");
        switchAmPm = (Switch)findViewById(R.id.switchAmPm);
        switchAmPm.setTextOff("AM");
        switchAmPm.setTextOn("PM");

        switchAmPm.setChecked(false);

        clockView.setOnClockChangeListener(new OnClockChangeListener() {
            @Override
            public void onTimeChange(String timeString) {
                ivHourDisplay.setText(
                        setDisplayTime(
                                clockView.getHour(clockView.getIvHourHand().getRotation()).toString(),
                                clockView.getMinute(clockView.getIvMinuteHand().getRotation()).toString()
                        )
                );
            }
        });


        bSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int HALF_DAY_MINUTES = 12*60;
                int arriveHour = clockView.getHour(clockView.getIvHourHand().getRotation());
                int arriveHourInMinutes = arriveHour*60;
                int arriveMinutesInMins = clockView.getMinute(clockView.getIvMinuteHand().getRotation());
                int arriveTimeInMins = arriveHourInMinutes + arriveMinutesInMins;
                int preparingTimeInMins = Integer.parseInt(etPreparingTimeInMins.getText().toString());

                int prepareTimeCorrection = HALF_DAY_MINUTES - preparingTimeInMins;
                int travelTimeInSecondsCorrection = HALF_DAY_MINUTES - travelTimeInSeconds/60;
                int alarmTimeInMinutes = arriveTimeInMins + prepareTimeCorrection + travelTimeInSecondsCorrection;
                int alarmHour;

                if (switchAmPm.isChecked()){
                    alarmHour = (alarmTimeInMinutes / 60) % 12 + 12;
                }
                else{
                    alarmHour = (alarmTimeInMinutes / 60) % 12;
                }

                int alarmMinute = alarmTimeInMinutes % 60;

//                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
//                i.putExtra(AlarmClock.EXTRA_HOUR, alarmHour);
//                i.putExtra(AlarmClock.EXTRA_MINUTES, alarmMinute);
//                i.putExtra(AlarmClock.EXTRA_DAYS, alarmMinute);
//                startActivity(i);

                Intent intent = new Intent(ClockFaceActivity.this, TripAlarmStartedService.class);
                intent.putExtra(TripAlarmStartedService.EXTRA_MESSAGE, "extra message");
                startService(intent);

            }
        });

//        ViewTreeObserver vto = clockView.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                clockView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int parentWidth = ((LinearLayout)clockView.getParent()).getWidth();
//                Log.d("PARENT WIDTH", String.valueOf(((LinearLayout)clockView.getParent()).getWidth()));
//                clockView.getLayoutParams().width = parentWidth;
//                clockView.getLayoutParams().height = parentWidth;
//            }
//        });

    }

    private String setDisplayTime(String hour, String minute){
        int intHour = Integer.parseInt(hour);
        int intMinute = Integer.parseInt(minute);

        String strHour = hour;
        String strMinute = minute;

        if(intHour<10){
            strHour = "0"+ Integer.toString(intHour);
        }
        if(intMinute<10){
            strMinute = "0"+ Integer.toString(intMinute);
        }

        return strHour+" : "+strMinute;
    }
}



