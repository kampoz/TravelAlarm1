package com.kaseka.boxmaptest1;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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
    private EditText etPreparingTimeInMins;
    private Switch switchAmPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        clockView = (ClockView)findViewById(R.id.rlClockParent);
        ivHourDisplay = (TextView)findViewById(R.id.tvHourDisplay);
        etPreparingTimeInMins = (EditText)findViewById(R.id.etPreparingTimeInMins);
        bSetAlarm = (Button) findViewById(R.id.bSetAlarm);
        etPreparingTimeInMins.setText("30");
        switchAmPm = (Switch)findViewById(R.id.switchAmPm);
        switchAmPm.setTextOn("PM");
        switchAmPm.setTextOff("AM");
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
                float arriveHour = clockView.getHour(clockView.getIvHourHand().getRotation());
                float arriveMinute = clockView.getMinute(clockView.getIvMinuteHand().getRotation());

                Integer preparingTimeInMins = Integer.parseInt(etPreparingTimeInMins.getText().toString());
                final int HALF_DAY_MILLIS = 43200000;

                long arriveHourInMillis = (long)arriveHour*60*60*1000;
                long arriveMinutesInMillis = (long)arriveMinute*60*1000;
                long arriveTimeInMillis = arriveHourInMillis + arriveMinutesInMillis;
                long preparingTimeInMillis = (long)preparingTimeInMins*60*1000;
                long alarmHourLong;

                        Log.d("Long type", "arriveHourInMillis "+String.valueOf(arriveHourInMillis));
                        Log.d("Long type", "arriveMinutesInMillis "+String.valueOf(arriveMinutesInMillis));
                        Log.d("Long type", "arriveTimeInMillis "+String.valueOf(arriveTimeInMillis));
                        Log.d("Long type", "preparingTimeInMillis "+String.valueOf(preparingTimeInMillis));

                long arriveTimeInMillisLong = (HALF_DAY_MILLIS + arriveTimeInMillis)%HALF_DAY_MILLIS;
                long prepareTimeCorrection = HALF_DAY_MILLIS - preparingTimeInMillis;
                long alarmTimeInMillis = arriveTimeInMillisLong + prepareTimeCorrection;

                if (switchAmPm.isChecked()){
                    alarmHourLong = (alarmTimeInMillis / (1000 * 60 * 60)) % 24;
                }
                else{
                    alarmHourLong = (alarmTimeInMillis / (1000 * 60 * 60)) % 12;
                }



                long alarmMinuteLong = (alarmTimeInMillis / (1000 * 60)) % 60;
                    Log.d("Long type", "alarmHourLong "+String.valueOf(alarmHourLong));
                    Log.d("Long type", "alarmMinuteLong "+String.valueOf(alarmMinuteLong));

                int alarmHour = (int)alarmHourLong;
                int alarmMinute = (int)alarmMinuteLong;

                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                i.putExtra(AlarmClock.EXTRA_HOUR, alarmHour);
                i.putExtra(AlarmClock.EXTRA_MINUTES, alarmMinute);
                startActivity(i);

//                String displayTime = String.valueOf(ivHourDisplay.getText());
//                String systemTime = String.valueOf(SystemClock.elapsedRealtime());
//                Log.d("bSetAlarm systemTime",systemTime);
//
//                alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
//                Intent intent = new Intent(AlarmClockActivity.this, AlarmReceiver.class);
//                alarmIntent = PendingIntent.getBroadcast(AlarmClockActivity.this, 0, intent, 0);
//                alarmMgr.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5 * 1000, alarmIntent);



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



