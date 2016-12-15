package com.kaseka.boxmaptest1.activity;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
//import android.icu.util.Calendar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;
import com.kaseka.boxmaptest1.listener.OnClockChangeListener;
import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.service.TripAlarmStartedService;
import com.kaseka.boxmaptest1.view.ClockView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

//import net.danlew.android.joda.JodaTimeAndroid;


public class ClockFaceActivity extends AppCompatActivity {

    private Context context = this;
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
    private int amPm = 0;

    private int alarmHour;
    private int alarmMinutes;

    private int travelTimeInSeconds = 0;
    private long alarmTimeInMillis;

    private int routeTimeInMinutes;
    private int preparingTimeInMins;

    private int alarmDayWeight = 0;
    private int todayDayWeight = 0;

    DateTime alarmDateTime;
    int goalHourInMins = 0;



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

            public TextView tvDialogAlarmHour;
            public TextView tvDialogWeekDay;
            public TextView tvDialogStartPoint;
            public TextView tvDialogDestinationPoint;
            public TextView tvDialogTransportMode;
            public TextView tvDialogGoalTime;
            public TextView tvDialogPreparingTime;
            public Button bDialogAlarmModification;
            public Button bDialogOK;

            @Override
            public void onClick(View v) {

                //Uzupełenianie dalej AlarmPOJO, poprzednio w MainActivity
                calculatingAlarmTimeInMillis();
                setAlarmPojoObject();



                Dialog dialog = new Dialog(ClockFaceActivity.this);
                dialog.setContentView(R.layout.alarm_dialog);
                dialog.setTitle("Alarm - ustawienia");
                dialog.show();

//                tvDialogAlarmHour = (TextView)findViewById(R.id.tvDialogAlarmHour);
//                tvDialogWeekDay = (TextView)findViewById(R.id.tvDialogWeekDay);
//                tvDialogStartPoint = (TextView)findViewById(R.id.tvDialogStartPoint);
//                tvDialogDestinationPoint = (TextView)findViewById(R.id.tvDialogDestinationPoint);
//                tvDialogTransportMode = (TextView)findViewById(R.id.tvDialogTransportMode);
//                tvDialogGoalTime = (TextView)findViewById(R.id.tvDialogGoalTime);
//                tvDialogPreparingTime = (TextView)findViewById(R.id.tvDialogPreparingTime);
//                bDialogAlarmModification = (Button)findViewById(R.id.bDialogAlarmModification);
//                bDialogOK  = (Button)findViewById(R.id.bDialogOK);
//
//
//                tvDialogAlarmHour.setText(AlarmPOJO.getAlarmHour()+" : "+AlarmPOJO.getAlarmMinute());
//                tvDialogWeekDay.setText(AlarmPOJO.getAlarmDayOfWeek());
//                tvDialogStartPoint.setText("z: "+AlarmPOJO.getStartPoint());
//                tvDialogDestinationPoint.setText("do: "+AlarmPOJO.getDestinationPoint());
//                tvDialogTransportMode.setText("transport: "+AlarmPOJO.getTransportMode());
//                tvDialogGoalTime.setText("na: "+AlarmPOJO.getGoalHourOfDay()+" : "+AlarmPOJO.getGoalMinute());
//                tvDialogPreparingTime.setText("przygotowanie: "+AlarmPOJO.getPreparingTimeInMins()+" min.");




//                bDialogAlarmModification.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlarmPOJO.setToNull();
//
//                        Intent startMainActivityIntent = new Intent(ClockFaceActivity.this, MainActivity.class);
//                        ClockFaceActivity.this.startActivity(startMainActivityIntent);
//                    }
//                });
//
//                bDialogOK.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlarmPOJO.insertAlarm();
//
//                        Intent intent = new Intent(ClockFaceActivity.this, TripAlarmStartedService.class);
//                        startService(intent);
//                    }
//                });



            }
        });


        bMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bMonday);
//                dayOfWeek = DayOfWeek.MONDAY.toString();
//                alarmDayWeight = 2;
                alarmDayWeight = DateTimeConstants.MONDAY;
            }
        });

        bTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bTuesday);
//                dayOfWeek = DayOfWeek.TUESDAY.toString();
//                alarmDayWeight = 3;
                alarmDayWeight = DateTimeConstants.TUESDAY;


            }
        });

        bWensday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bWensday);
//                dayOfWeek = DayOfWeek.WENSDAY.toString();
//                alarmDayWeight = 4;
                alarmDayWeight = DateTimeConstants.WEDNESDAY;
            }
        });

        bThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bThursday);
//                dayOfWeek = DayOfWeek.THURSDAY.toString();
//                alarmDayWeight = 5;
                alarmDayWeight = DateTimeConstants.THURSDAY;
            }
        });

        bFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bFriday);
//                dayOfWeek = DayOfWeek.FRIDAY.toString();
//                alarmDayWeight = 6;
                alarmDayWeight = DateTimeConstants.FRIDAY;
            }
        });

        bSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bSaturday);
//                dayOfWeek = DayOfWeek.SATURDAY.toString();
//                alarmDayWeight = 7;
                alarmDayWeight = DateTimeConstants.SATURDAY;
            }
        });

        bSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDaysReaction(bSunday);
//                dayOfWeek = DayOfWeek.SUNDAY.toString();
//                alarmDayWeight = 1;
                alarmDayWeight = DateTimeConstants.SUNDAY;
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

    private void setAlarmPojoObject(){
        AlarmPOJO.setAlarmHour(alarmHour);
        AlarmPOJO.setAlarmMinute(alarmMinutes);
        AlarmPOJO.setAlarmDayOfWeek(dayOfWeek);
        AlarmPOJO.setPreparingTimeInMins(preparingTimeInMins);
        AlarmPOJO.setAmPm(amPm);
        AlarmPOJO.setAlarmTimeInMillis(alarmTimeInMillis);
        AlarmPOJO.setAlarmDateTimeData(alarmDateTime.toString());
        AlarmPOJO.setGoalHourOfDay(String.valueOf(goalHourInMins/60));
        AlarmPOJO.setGoalMinute(clockMinute);

        Log.d("timetest", "AlarmPOJO.getAlarmDateTimeData(): " + AlarmPOJO.getAlarmDateTimeData());

        //sprawdzenie parsowania ze Stringa na DateTime
        DateTime dateTimeParseFromString = DateTime.parse(AlarmPOJO.getAlarmDateTimeData());
        Log.d("timetest", "dateTimeParseFromString: " + dateTimeParseFromString);
    }

    private void calculatingAlarmTimeInMillis(){

        //dzien budzenia jako liczba 1-7
        int alarmWeekDay = alarmDayWeight;

        //Zamiana czasu zegarka na minuty, dla latwiejszych obliczeń
        int ampm = amPm;
        clockHourInt = Integer.parseInt(clockHour);

        goalHourInMins = clockHourInt*60;
        if(ampm ==1){
            goalHourInMins = (clockHourInt + 12)*60 ;
        }

        int goalMinute = Integer.parseInt(clockMinute);
        int goalTimeInMins = goalHourInMins + goalMinute;


        int routeTimeInSeconds = AlarmPOJO.getRouteTimeInSeconds();
        //routeTimeInMinutes = routeTimeInSeconds / 60;
        routeTimeInMinutes = 15;

        //czas przygotowania
        //preparingTimeInMins = Integer.parseInt(etPreparingTimeInMins.getText().toString());
        preparingTimeInMins = 15;


        ///////////////////////METODA II BIBL. JODA-TIME///////////////////////

        DateTime currentDaleTime = new DateTime();
        int minuteOfHour = currentDaleTime.getMinuteOfHour();
        int hourOfDay = currentDaleTime.getHourOfDay();
        int dayOfWeek = currentDaleTime.getDayOfWeek();
        int currentDayTimeInMinutes = hourOfDay*60 + minuteOfHour;

        int minutesToAdd = 1440*((alarmWeekDay -dayOfWeek +7)%7) - currentDayTimeInMinutes + goalTimeInMins - routeTimeInMinutes - preparingTimeInMins;

        alarmDateTime = currentDaleTime.plusMinutes(minutesToAdd);
        alarmHour = alarmDateTime.getHourOfDay();
        alarmMinutes = alarmDateTime.getMinuteOfHour();
        currentDaleTime.getMinuteOfHour();



        Log.d("timetest", "######################################################################");
        Log.d("timetest", "AlarmPOJO.getRouteTimeLabel(): " + AlarmPOJO.getRouteTimeLabel());

        Log.d("timetest", "............................................................");
        Log.d("timetest", "goalTimeInMins: " + goalTimeInMins);
        Log.d("timetest", "preparingTimeInMins: " + preparingTimeInMins);
        Log.d("timetest", "routeTimeInMinutes: " + routeTimeInMinutes);
        Log.d("timetest", "............................................................");
        Log.d("timetest", "ampm: " + ampm);
        Log.d("timetest", "clockHourInt: " + clockHourInt);
        Log.d("timetest", "goalHourInMins: " + goalHourInMins/60);
        Log.d("timetest", "goalMinute: " + goalMinute);
        Log.d("timetest", "goalHourInMins: " + goalHourInMins);
        Log.d("timetest", "............................................................");
        Log.d("timetest", "alarmHour: " + alarmHour);
        Log.d("timetest", "alarmMinutes: " + alarmMinutes);
        Log.d("timetest", "alarmDayWeight: " + alarmDayWeight);
        Log.d("timetest", "currentDaleTime: " + currentDaleTime.toString());
        Log.d("timetest", "minutesToAdd: " + minutesToAdd);
        Log.d("timetest", "alarmDateTime: " + alarmDateTime);
        Log.d("timetest", "alarmDateTime: " + alarmDateTime.getDayOfWeek());
        Log.d("timetest", "AlarmPOJO.getAlarmDateTimeData(): " + AlarmPOJO.getAlarmDateTimeData());

        Log.d("timetest", "_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");
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



