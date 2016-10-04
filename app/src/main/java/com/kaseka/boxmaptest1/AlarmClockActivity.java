package com.kaseka.boxmaptest1;


import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import static android.R.attr.x;
import static android.R.attr.y;

public class AlarmClockActivity extends AppCompatActivity {

    private TimePicker timePickerSetArriveTime;
    private Calendar calendar;
    private String format = "";
    ClockView clockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        clockView = (ClockView)findViewById(R.id.rlClockParent);
        clockView.setOnClockChangeListener(new OnClockChangeListener() {
            @Override
            public void onTimeChange(String timeString) {
                Log.d("Godz kąt Hour", String.valueOf(clockView.getHourAngle()));
                Log.d("Godz kąt Minute", String.valueOf(clockView.getMinuteAngle()));
                Log.d("Godz", timeString);

            }
        });

    }
}



