package com.kaseka.boxmaptest1.activity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
//import android.icu.util.Calendar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;
import com.kaseka.boxmaptest1.global.DayOfWeek;
import com.kaseka.boxmaptest1.listener.OnClockChangeListener;
import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.service.TripAlarmStartedService;
import com.kaseka.boxmaptest1.view.ClockView;

//import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.Chronology;
import org.joda.time.base.AbstractDateTime;


public class ClockFaceActivity extends AppCompatActivity {

    private TextView ivHourDisplay;
    private Button bSetAlarm;
    ClockView clockView;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private EditText etPreparingTimeInMins;

    private Integer clockHourInt;
    private Integer clockMinuteInt;
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

    private Button bAm;
    private Button bPm;
    private int amPm;

    private int travelTimeInSeconds = 0;
    private long wakeUpTimeInMillis;

    private int alarmDayWeight = 0;
    private int todayDayWeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_clock_face);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        //travelTimeInSeconds = bundle.getInt("travelTimeInSeconds");

        clockView = (ClockView)findViewById(R.id.rlClockParent);
        ivHourDisplay = (TextView)findViewById(R.id.tvHourDisplay);
        etPreparingTimeInMins = (EditText)findViewById(R.id.etPreparingTimeInMins);
        etPreparingTimeInMins.setText("0");
        bSetAlarm = (Button) findViewById(R.id.bSetAlarm);

        bMonday = (Button) findViewById(R.id.bMonday);
        bTuesday = (Button) findViewById(R.id.bTuesday);
        bWensday = (Button) findViewById(R.id.bWensday);
        bThursday = (Button) findViewById(R.id.bThursday);
        bFriday = (Button) findViewById(R.id.bFriday);
        bSaturday = (Button) findViewById(R.id.bSaturday);
        bSunday = (Button) findViewById(R.id.bSunday);

        bAm = (Button) findViewById(R.id.bAM);
        bPm = (Button) findViewById(R.id.bPM);


        int hours = new Time(System.currentTimeMillis()).getHours();
        Log.d("TimeData", "hours: "+hours);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        Log.d("TimeData", "dayOfWeek: "+day);


        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Log.d("TimeData", "hour: "+hour);

        int minute = calendar.get(Calendar.MINUTE);
        Log.d("TimeData", "minute: "+minute);

        //funkcja zwraca 0 dla AM i 1 dla PM
        day = calendar.get(Calendar.AM_PM);
        Log.d("TimeData", "AM/PM: "+day);


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
                Integer preparingTimeInMins = Integer.parseInt(etPreparingTimeInMins.getText().toString());

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

                calculatingWakeUpTimeInMilis();

                AlarmPOJO.alarmHour = clockHour;
                AlarmPOJO.alarmMinute = clockMinute;
                //AlarmPOJO.alarmDay =
                AlarmPOJO.dayOfWeek = dayOfWeek;
                AlarmPOJO.preparingTimeInMins = Integer.parseInt(etPreparingTimeInMins.getText().toString());
                AlarmPOJO.amPm = amPm;
                AlarmPOJO.wakeUpTimeInMillis = wakeUpTimeInMillis;



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
                buttonsDaysReaction(bMonday);
                dayOfWeek = DayOfWeek.MONDAY.toString();
                alarmDayWeight = 2;
            }
        });

        bTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bTuesday);
                dayOfWeek = DayOfWeek.TUESDAY.toString();
                alarmDayWeight = 3;

            }
        });

        bWensday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bWensday);
                dayOfWeek = DayOfWeek.WENSDAY.toString();
                alarmDayWeight = 4;
            }
        });

        bThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bThursday);
                dayOfWeek = DayOfWeek.THURSDAY.toString();
                alarmDayWeight = 5;
            }
        });

        bFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bFriday);
                dayOfWeek = DayOfWeek.FRIDAY.toString();
                alarmDayWeight = 6;
            }
        });

        bSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bSaturday);
                dayOfWeek = DayOfWeek.SATURDAY.toString();
                alarmDayWeight = 7;
            }
        });

        bSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bSunday);
                dayOfWeek = DayOfWeek.SUNDAY.toString();
                alarmDayWeight = 1;
            }
        });

        bAm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsAmPmReaction(bAm);
                amPm = 0;
            }
        });

        bPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsAmPmReaction(bPm);
                amPm = 1;
            }
        });
    }

    private void calculatingWakeUpTimeInMilis(){

        Calendar calendar = Calendar.getInstance();

        //Obecny czas: dzien tyg jako liczba 1-7; godzina, minuta, czas obecny w min.

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        int todayWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        int nowTimeInMins = nowHour*60 + nowMinute;

        //dzien budzenia jako liczba 1-7
        int wakeUpWeekDay = alarmDayWeight;

        //roznica miedzy dniem budzenia i dzisiejszym
        int differcenceBetweenDays = (wakeUpWeekDay-todayWeekDay+7)%7;
        int differcenceBetweenDaysInMinutes = differcenceBetweenDays*24*60;

        //czas przygotowania
        int preparingTimeInMins = Integer.parseInt(etPreparingTimeInMins.getText().toString());

        //Zamiana czasu zegarka na minuty, dla latwiejszych obliczeń
        int ampm = amPm;
        clockHourInt = Integer.parseInt(clockHour);

        int goalHourInMins = clockHourInt*60;
        if(ampm == 1){
            goalHourInMins = (clockHourInt + 12)*60 ;
        }
        clockMinuteInt = Integer.parseInt(clockMinute);
        int goalMinute = clockMinuteInt;
        int goalTimeInMins = goalHourInMins + goalMinute;

        long currenttimeLong = System.currentTimeMillis();
        int timeSubtractionResultInMims = goalTimeInMins - nowTimeInMins - preparingTimeInMins;

        int dayDifferenceInMins = differcenceBetweenDays *24 *60;
        long allTimeSubtractionResultInMiliis = (dayDifferenceInMins+timeSubtractionResultInMims) *60 *1000;

        double differenceInDays = (double)allTimeSubtractionResultInMiliis/1000/60/60/24;

        wakeUpTimeInMillis = currenttimeLong + allTimeSubtractionResultInMiliis;


        String wakeUpDay = dayOfWeek;

        int minutesFromWakeUpTime = (int) ((wakeUpTimeInMillis / (1000*60)) % 60);
        int hourFromWakeUpTime   = (int) ((wakeUpTimeInMillis / (1000*60*60)) % 24);



        Log.d("timetest", "============================================================");
        Log.d("timetest", "dayOfTheWeek: " + dayOfTheWeek);
        Log.d("timetest", "todayWeekDay: " + todayWeekDay);
        Log.d("timetest", "nowHour: " + nowHour);
        Log.d("timetest", "nowMinute: " + nowMinute);
        Log.d("timetest", "differcenceBetweenDays: " + differcenceBetweenDays);
        Log.d("timetest", "preparingTimeInMins: " + preparingTimeInMins);
        Log.d("timetest", "AlarmPOJO.routeTime: " + AlarmPOJO.routeTime);
        Log.d("timetest", "ampm: " + ampm);
        Log.d("timetest", "goalHour: " + clockHourInt);
        Log.d("timetest", "goalHourInMins: " + goalHourInMins);
        Log.d("timetest", "goalMinute: " + goalMinute);
        Log.d("timetest", "goalTimeInMins: " + goalTimeInMins);
        Log.d("timetest", "currenttimeLong: " + currenttimeLong);
        Log.d("timetest", "timeSubtractionResultInMims: " + timeSubtractionResultInMims);
        Log.d("timetest", "allTimeSubtractionResultInMiliis: " + allTimeSubtractionResultInMiliis);
        Log.d("timetest", "wakeUpTimeInMillis: " + wakeUpTimeInMillis);
        Log.d("timetest", "differenceInDays: " + differenceInDays);

        Log.d("timetest", "hourFromWakeUpTime: " + hourFromWakeUpTime);
        Log.d("timetest", "minutesFromWakeUpTime: " + minutesFromWakeUpTime);
        Log.d("timetest", "wakeUpDay: " + wakeUpDay);

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

    private void buttonsDaysReaction(Button button){

        bMonday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bTuesday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bWensday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bThursday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bFriday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bSaturday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bSunday.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyLightGreen));
    }

    private void buttonsAmPmReaction(Button button){
        bAm.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bPm.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyYellow));
    }
}



