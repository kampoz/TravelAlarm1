package com.kaseka.boxmaptest1;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;


public class AlarmClockActivity extends AppCompatActivity {

    private TimePicker timePickerSetArriveTime;
    private Calendar calendar;
    private String format = "";
    private TextView ivHourDisplay;
    private Button bSetAlarm;
    ClockView clockView;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        clockView = (ClockView)findViewById(R.id.rlClockParent);
        this.ivHourDisplay = (TextView)findViewById(R.id.tvHourDisplay);
        bSetAlarm = (Button) findViewById(R.id.bSetAlarm);
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
                String displayTime = String.valueOf(ivHourDisplay.getText());

                alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(AlarmClockActivity.this, MainActivity.class);
                alarmIntent = PendingIntent.getBroadcast(AlarmClockActivity.this, 0, intent, 0);

                alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() +
                                5 * 1000, alarmIntent);

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



