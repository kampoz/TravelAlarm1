package com.kaseka.boxmaptest1;


import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import static android.R.attr.x;
import static android.R.attr.y;

public class AlarmClockActivity extends AppCompatActivity {

    private TimePicker timePickerSetArriveTime;
    private Calendar calendar;
    private String format = "";
    private TextView ivHourDisplay;
    ClockView clockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        clockView = (ClockView)findViewById(R.id.rlClockParent);
        this.ivHourDisplay = (TextView)findViewById(R.id.tvHourDisplay);
        clockView.setOnClockChangeListener(new OnClockChangeListener() {
            @Override
            public void onTimeChange(String timeString) {
                Log.d("Godz kąt Hour", String.valueOf(clockView.getIvHourHand()));
                Log.d("Godz kąt Minute", String.valueOf(clockView.getIvMinuteHand()));
                Log.d("Godz", timeString);
                //ivHourDisplay.setText(clockView.getHour()+" : "+clockView.getMinute());
                ivHourDisplay.setText(
                        setDisplayTime(
                                clockView.getHour(clockView.getIvHourHand().getRotation()).toString(),
                                clockView.getMinute(clockView.getIvMinuteHand().getRotation()).toString()
                        )
                );
            }
        });

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



