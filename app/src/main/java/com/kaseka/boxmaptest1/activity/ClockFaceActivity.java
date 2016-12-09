package com.kaseka.boxmaptest1.activity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
//import android.icu.util.Calendar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;
import com.kaseka.boxmaptest1.global.DayOfWeek;
import com.kaseka.boxmaptest1.listener.OnClockChangeListener;
import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.service.TripAlarmStartedService;
import com.kaseka.boxmaptest1.view.ClockView;

import net.danlew.android.joda.JodaTimeAndroid;

import java.security.PrivateKey;
import java.util.Date;


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
    private String clockHour;
    private String clockMinute;

    private Button bMonday;
    private Button bTuesday;
    private Button bWensday;
    private Button bThursday;
    private Button bFriday;
    private Button bSaturday;
    private Button bSunday;
    private String dayOfWeek;

    int travelTimeInSeconds = 0;

    private int alarmDayWeight = 0;
    private int todayDayWeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_clock_face);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        //travelTimeInSeconds = bundle.getInt("travelTimeInSeconds");

        clockView = (ClockView)findViewById(R.id.rlClockParent);
        ivHourDisplay = (TextView)findViewById(R.id.tvHourDisplay);
        etPreparingTimeInMins = (EditText)findViewById(R.id.etPreparingTimeInMins);
        bSetAlarm = (Button) findViewById(R.id.bSetAlarm);

        bMonday = (Button) findViewById(R.id.bMonday);
        bTuesday = (Button) findViewById(R.id.bTuesday);
        bWensday = (Button) findViewById(R.id.bWensday);
        bThursday = (Button) findViewById(R.id.bThursday);
        bFriday = (Button) findViewById(R.id.bFriday);
        bSaturday = (Button) findViewById(R.id.bSaturday);
        bSunday= (Button) findViewById(R.id.bSunday);

//        calendar = Calendar.getInstance();
//        int currentHouroFDay = calendar.HOUR_OF_DAY;
//        int currentHour = calendar.HOUR;
//
//        Log.d("currentHourofDay", "currentHourofDay: "+currentHouroFDay);
//        Log.d("currentHour", "currentHour: "+currentHour);

        int hours = new Time(System.currentTimeMillis()).getHours();
        Log.d("TimeData", "hours: "+hours);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d("TimeData", "dayOfWeek: "+day);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        Log.d("TimeData", "dayOfTheWeek: "+dayOfTheWeek);

        int dayOfWeekNr = (calendar.get(Calendar.DAY_OF_WEEK));
        Log.d("TimeData", "dayOfTheWeekNr: "+dayOfWeekNr);

//        SimpleDateFormat dow = new SimpleDateFormat("dddd");
//        Date e = new Date();
//        String dayOfTheWeekNr = dow.format(e);
//        Log.d("TimeData", "dayOfTheWeekNr: "+dayOfTheWeekNr);




        clockView.setOnClockChangeListener(new OnClockChangeListener() {
            @Override
            public void onTimeChange(String timeString) {
                clockHour = clockView.getHour(clockView.getIvHourHand().getRotation()).toString();
                clockMinute = clockView.getMinute(clockView.getIvMinuteHand().getRotation()).toString();

                ivHourDisplay.setText(
                        setDisplayTime(
                                clockHour,
                                clockMinute
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

                //OPISAĆ AQM I PM!!
//                if (switchAmPm.isChecked()){
//                    alarmHour = (alarmTimeInMinutes / 60) % 12 + 12;
//                }
//                else{
//                    alarmHour = (alarmTimeInMinutes / 60) % 12;
//                }
//
//                int alarmMinute = alarmTimeInMinutes % 60;

//                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
//                i.putExtra(AlarmClock.EXTRA_HOUR, alarmHour);
//                i.putExtra(AlarmClock.EXTRA_MINUTES, alarmMinute);
//                i.putExtra(AlarmClock.EXTRA_DAYS, alarmMinute);
//                startActivity(i);

                //Uzupełenianie dalej AlarmPOJO, poprzednio w MainActivity
                AlarmPOJO.alarmHour = clockHour;
                AlarmPOJO.alarmMinute = clockMinute;
                //AlarmPOJO.alarmDay =
                AlarmPOJO.dayOfWeek = dayOfWeek;
                AlarmPOJO.preparingTimeInMins = Integer.parseInt(etPreparingTimeInMins.getText().toString());


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

        bMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(bMonday);
                dayOfWeek = DayOfWeek.MONDAY.toString();
                alarmDayWeight = 1;
            }
        });

        bTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(bTuesday);
                dayOfWeek = DayOfWeek.TUESDAY.toString();
                alarmDayWeight = 2;

            }
        });

        bWensday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(bWensday);
                dayOfWeek = DayOfWeek.WENSDAY.toString();
                alarmDayWeight = 3;
            }
        });

        bThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(bThursday);
                dayOfWeek = DayOfWeek.THURSDAY.toString();
                alarmDayWeight = 4;
            }
        });

        bFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(bFriday);
                dayOfWeek = DayOfWeek.FRIDAY.toString();
                alarmDayWeight = 5;
            }
        });

        bSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(bSaturday);
                dayOfWeek = DayOfWeek.SATURDAY.toString();
                alarmDayWeight = 6;
            }
        });

        bSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(bSunday);
                dayOfWeek = DayOfWeek.SUNDAY.toString();
                alarmDayWeight = 7;
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

    private void buttonsReaction(Button button){

        bMonday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bTuesday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bWensday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bThursday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bFriday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bSaturday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bSunday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));

        button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyLightGreen));
    }
}



