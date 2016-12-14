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

import org.joda.time.DateTime;

//import net.danlew.android.joda.JodaTimeAndroid;


public class ClockFaceActivity extends AppCompatActivity {

    private TextView ivHourDisplay;
    private Button bSetAlarm;
    ClockView clockView;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private EditText etPreparingTimeInMins;

    private Integer clockHourInt = 0;
    private Integer clockMinuteInt = 0;
    private String clockHour = "0";
    private String clockMinute = "0";

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

    private int alarmHour;
    private int alarmMinutes;

    private int travelTimeInSeconds = 0;
    private long alarmTimeInMillis;

    private int routeTimeInMinutes;
    private int preparingTimeInMins;

    private int alarmDayWeight = 0;
    private int todayDayWeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_clock_face);
        getSupportActionBar().hide();


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
        Log.d("TimeData", "wakeUpDayOfWeek: "+day);


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


                //Uzupełenianie dalej AlarmPOJO, poprzednio w MainActivity

                calculatingAlarmTimeInMillis();
                setAlarmPojoObjcest();

                Intent intent = new Intent(ClockFaceActivity.this, TripAlarmStartedService.class);
                intent.putExtra(TripAlarmStartedService.EXTRA_MESSAGE, "extra message");
                startService(intent);

            }
        });


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

    private void setAlarmPojoObjcest(){
        AlarmPOJO.setAlarmHour(alarmHour);
        AlarmPOJO.setAlarmMinute(alarmMinutes);
        AlarmPOJO.setAlarmDayOfWeek(dayOfWeek);
        AlarmPOJO.setPreparingTimeInMins(preparingTimeInMins);
        AlarmPOJO.setAmPm(amPm);
        AlarmPOJO.setAlarmTimeInMillis(alarmTimeInMillis);


    }

    private void calculatingAlarmTimeInMillis(){

        Calendar calendar = Calendar.getInstance();
        long currenttimeLong = System.currentTimeMillis();

        //Obecny czas: dzien tyg jako liczba 1-7; godzina, minuta, czas obecny w min.
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String currentDayOfTheWeek = sdf.format(d);
        int todayWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        int nowTimeInMins = nowHour*60 + nowMinute;

        //dzien budzenia jako liczba 1-7
        int alarmWeekDay = alarmDayWeight;

        //roznica miedzy dniem budzenia i dzisiejszym
        int differcenceBetweenDays = (alarmWeekDay-todayWeekDay+7)%7;

        int dayDifferenceInMins = differcenceBetweenDays *24 *60;

        //czas przygotowania
        //preparingTimeInMins = Integer.parseInt(etPreparingTimeInMins.getText().toString());
        preparingTimeInMins = 15;

        //Zamiana czasu zegarka na minuty, dla latwiejszych obliczeń
        int ampm = amPm;
        clockHourInt = Integer.parseInt(clockHour);

        int goalHourInMins = clockHourInt*60;
        if(ampm ==1){
            goalHourInMins = (clockHourInt + 12)*60 ;
        }

        int goalMinute = Integer.parseInt(clockMinute);
        int goalTimeInMins = goalHourInMins + goalMinute;

        int hoursSubstractionInMins = goalTimeInMins - nowTimeInMins;

        long currenttimeLongPlushoursSubstraction =  hoursSubstractionInMins*60*1000 + currenttimeLong;
        long currenttimeLongPlushoursSubstractionH =  (currenttimeLongPlushoursSubstraction / (1000*60)) % 60;
        long currenttimeLongPlushoursSubstractionMin =  (currenttimeLongPlushoursSubstraction / (1000*60*60)) % 24;

        int firstSumInMins = dayDifferenceInMins + hoursSubstractionInMins;

        //routeTimeInMinutes = routeTimeInSeconds / 60;
        routeTimeInMinutes = 15;

        int secondSumInMins = firstSumInMins - preparingTimeInMins - routeTimeInMinutes;
        long secondSumInMillis = secondSumInMins * 60 *1000;



        int routeTimeInSeconds = AlarmPOJO.getRouteTimeInSeconds();



        alarmTimeInMillis = currenttimeLong + secondSumInMillis;


        String alarmDayOfWeek = dayOfWeek;

        alarmMinutes = (int) ((alarmTimeInMillis / (1000*60)) % 60);

        alarmHour   = (int) ((alarmTimeInMillis / (1000*60*60)) % 24);

        //zwikszenie godziny alarmu o jedna godzine, bo taki jest bład
        int alarmHourCorrection =(int) (((alarmTimeInMillis + 1000*60*60) / (1000*60*60)) % 24);

        ///////////////////////METODA II BIBL. JODA-TIME///////////////////////

        DateTime currentDaleTime = new DateTime();
        int minutesToAdd = 1440*((alarmWeekDay-todayWeekDay+7)%7)-nowTimeInMins + goalTimeInMins - routeTimeInMinutes - preparingTimeInMins;

        DateTime alarmDateTime = new DateTime();
        alarmDateTime.plusMinutes(minutesToAdd);
        alarmDateTime.plusMinutes(minutesToAdd).getDayOfWeek();

        Log.d("timetest", "*****************************************************************");
        Log.d("timetest", "currentDayOfTheWeek: " + currentDayOfTheWeek);
        Log.d("timetest", "todayWeekDay: " + todayWeekDay);
        Log.d("timetest", "nowHour: " + nowHour);
        Log.d("timetest", "nowMinute: " + nowMinute);
        Log.d("timetest", "nowTimeInMins: " + nowTimeInMins);
        Log.d("timetest", "differcenceBetweenDays: " + differcenceBetweenDays);
        Log.d("timetest", "AlarmPOJO.getRouteTimeLabel(): " + AlarmPOJO.getRouteTimeLabel());

        Log.d("timetest", "............................................................");
        Log.d("timetest", "goalTimeInMins: " + goalTimeInMins);
        Log.d("timetest", "goalTimeInMins/h: " + ((goalTimeInMins /60)%24));
        Log.d("timetest", "goalTimeInMins/min: " + (goalTimeInMins%60));
        Log.d("timetest", "nowTimeInMins: " + nowTimeInMins);
        Log.d("timetest", "preparingTimeInMins: " + preparingTimeInMins);
        Log.d("timetest", "routeTimeInMinutes: " + routeTimeInMinutes);

        Log.d("timetest", "............................................................");

        Log.d("timetest", "ampm: " + ampm);
        Log.d("timetest", "clockHourInt: " + clockHourInt);
        Log.d("timetest", "goalHourInMins: " + goalHourInMins/60);
        Log.d("timetest", "goalMinute: " + goalMinute);
        Log.d("timetest", "goalHourInMins: " + goalHourInMins);
        Log.d("timetest", "currenttimeLong: " + currenttimeLong);

        Log.d("timetest", "currenttimeLongPlushoursSubstraction: " + currenttimeLongPlushoursSubstraction);
        Log.d("timetest", "currenttimeLongPlushoursSubstractionH: " + currenttimeLongPlushoursSubstractionH);
        Log.d("timetest", "currenttimeLongPlushoursSubstractionMin: " + currenttimeLongPlushoursSubstractionMin);

        Log.d("timetest", "AlarmTimeInMillis: " + alarmTimeInMillis);
        //Log.d("timetest", "differenceInDays: " + differenceInDays);
        Log.d("timetest", "hoursSubstractionInMins: " + hoursSubstractionInMins);
        Log.d("timetest", "hoursSubstractionInMins/h: " + ((hoursSubstractionInMins/60)%24));
        Log.d("timetest", "hoursSubstractionInMins/min: " + (hoursSubstractionInMins%60));
        Log.d("timetest", "firstSumInMins: " + firstSumInMins);
        Log.d("timetest", "secondSumInMins: " + secondSumInMins);

        Log.d("timetest", "alarmHour: " + alarmHour);
        Log.d("timetest", "alarmHourCorrection: " + alarmHourCorrection);
        Log.d("timetest", "alarmMinutes: " + alarmMinutes);
        Log.d("timetest", "alarmDayOfWeek: " + alarmDayOfWeek);
        Log.d("timetest", "currentDaleTime: " + currentDaleTime.toString());
        Log.d("timetest", "minutesToAdd: " + minutesToAdd);
        Log.d("timetest", "Day of week DateTime: " + alarmDateTime.plusMinutes(minutesToAdd));
        Log.d("timetest", "alarmDateTime: " + alarmDateTime.plusMinutes(minutesToAdd).getDayOfWeek());
        Log.d("timetest", "*********************************************************************");
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



