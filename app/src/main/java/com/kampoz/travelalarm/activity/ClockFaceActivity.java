package com.kampoz.travelalarm.activity;


import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kampoz.travelalarm.R;
import com.kampoz.travelalarm.data.realm.AlarmPOJO;
import com.kampoz.travelalarm.dialog.AlarmDialogFragment;
import com.kampoz.travelalarm.global.DayOfWeek;
import com.kampoz.travelalarm.helper.Cache;
import com.kampoz.travelalarm.helper.MyDisplayTimeHelper;
import com.kampoz.travelalarm.listener.OnClockChangeListener;
import com.kampoz.travelalarm.view.ClockView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

//import android.icu.util.Calendar;

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
    private int preparingTimeInMins = 0;

    private int alarmDayWeight = 0;
    private int todayDayWeight = 0;

    DateTime alarmDateTime;
    int goalHourInMins = 0;

    private Toolbar toolbar;
    private final long ONE_WEEK_IN_MILLIS = 1000*60*60*24*7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_face);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Set arrive time");
        setSupportActionBar(toolbar);

        clockView = (ClockView)findViewById(R.id.rlClockParent);
        ivHourDisplay = (TextView)findViewById(R.id.tvHourDisplay);
        etPreparingTimeInMins = (EditText)findViewById(R.id.etPreparingTimeInMins);
        //etPreparingTimeInMins.setHint("Preparing time");
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
                        MyDisplayTimeHelper.setDisplayTime(clockHour, clockMinute)
                );
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
                amPm = DateTimeConstants.AM;
            }
        });

        bPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsAmPmReaction(bPm);
                amPm = DateTimeConstants.PM;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_add_alarm){
            Intent startAlarmsListActivityIntent = new Intent(this, MainActivity.class);
            this.startActivity(startAlarmsListActivityIntent);
            this.finish();
        }

        if (id==R.id.action_show_alarms_list){
            Intent startAlarmsListActivityIntent = new Intent(this, AlarmsListActivity.class);
            this.startActivity(startAlarmsListActivityIntent);
            this.finish();
        }

        if (id==R.id.action_setting){

        }

        if (id==R.id.action_about){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Copyright \u00a9 2017\nKamil Poznakowski\nkampoznak@gmail.com");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


    public void showDialog(View v){
        calculatingAlarmTimeInMillis();
        setAlarmPojoObject();
        FragmentManager manager = getFragmentManager();
        AlarmDialogFragment myDialog = new AlarmDialogFragment();
        myDialog.setContext(this);
        myDialog.show(manager, "myDialog");
    }

    private void setAlarmPojoObject(){
        AlarmPOJO alarmPOJO = Cache.getAlarmPOJO();

        alarmPOJO.setSecondPhaseData(
                alarmHour,
                alarmMinutes,
                dayOfWeek,
                preparingTimeInMins,
                amPm,
                alarmTimeInMillis,
                alarmDateTime.toString(),
                goalHourInMins/60,
                Integer.parseInt(clockMinute)
        );



        Log.d("timetest", "AlarmPOJO.getAlarmDateTimeData(): " + alarmPOJO.getAlarmDateTimeData());

        //sprawdzenie parsowania ze Stringa na DateTime
        DateTime dateTimeParseFromString = DateTime.parse(alarmPOJO.getAlarmDateTimeData());
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


        int routeTimeInSeconds = Cache.getAlarmPOJO().getRouteTimeInSeconds();
        routeTimeInMinutes = routeTimeInSeconds / 60;
        //routeTimeInMinutes = 15;

        //czas przygotowania
        preparingTimeInMins = Integer.parseInt(etPreparingTimeInMins.getText().toString());
        //preparingTimeInMins = 15;

        ///////////////////////METODA II BIBL. JODA-TIME///////////////////////
        DateTime currentDaleTime = new DateTime();
        int minuteOfHour = currentDaleTime.getMinuteOfHour();
        int hourOfDay = currentDaleTime.getHourOfDay();
        int dayOfWeek = currentDaleTime.getDayOfWeek();
        int currentDayTimeInMinutes = hourOfDay*60 + minuteOfHour;
        int minutesToAdd = 1440*((alarmWeekDay - dayOfWeek +7)%7) - currentDayTimeInMinutes + goalTimeInMins - routeTimeInMinutes - preparingTimeInMins;

        alarmDateTime = currentDaleTime.plusMinutes(minutesToAdd);
        alarmHour = alarmDateTime.getHourOfDay();
        alarmMinutes = alarmDateTime.getMinuteOfHour();
        alarmTimeInMillis = alarmDateTime.getMillis();
        long currentTimeInMillis = (new DateTime()).getMillis();
        if(currentTimeInMillis >= alarmTimeInMillis){
            alarmDateTime = alarmDateTime.plusDays(7);
            alarmTimeInMillis = alarmDateTime.getMillis();
        }
        this.dayOfWeek = getDayName(alarmDateTime.getDayOfWeek());

        //currentDaleTime.getMinuteOfHour();

//        Log.d("timetest", "######################################################################");
//        Log.d("timetest", "AlarmPOJO.getRouteTimeLabel(): " + AlarmPOJO.getRouteTimeLabel());
//
//        Log.d("timetest", "............................................................");
//        Log.d("timetest", "goalTimeInMins: " + goalTimeInMins);
//        Log.d("timetest", "preparingTimeInMins: " + preparingTimeInMins);
//        Log.d("timetest", "routeTimeInMinutes: " + routeTimeInMinutes);
//        Log.d("timetest", "............................................................");
//        Log.d("timetest", "ampm: " + ampm);
//        Log.d("timetest", "clockHourInt: " + clockHourInt);
//        Log.d("timetest", "goalHourInMins: " + goalHourInMins/60);
//        Log.d("timetest", "goalMinute: " + goalMinute);
//        Log.d("timetest", "goalHourInMins: " + goalHourInMins);
//        Log.d("timetest", "............................................................");
//        Log.d("timetest", "alarmHour: " + alarmHour);
//        Log.d("timetest", "alarmMinutes: " + alarmMinutes);
//        Log.d("timetest", "alarmDayWeight: " + alarmDayWeight);
//        Log.d("timetest", "currentDaleTime: " + currentDaleTime.toString());
//        Log.d("timetest", "minutesToAdd: " + minutesToAdd);
//        Log.d("timetest", "alarmDateTime: " + alarmDateTime);
//        Log.d("timetest", "alarmDateTime.getDayOfWeek(): " + alarmDateTime.getDayOfWeek());
//        Log.d("timetest", "AlarmPOJO.getAlarmDateTimeData(): " + AlarmPOJO.getAlarmDateTimeData());
//
//        Log.d("timetest", "_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");
    }

    public static String setDisplayTime(String hour, String minute){
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
        button.setBackgroundResource(R.drawable.light_green_button_shape);
        //button.setTextColor(ContextCompat.getColor(this, R.color.colorMyBlack));
    }

    private void buttonsAmPmReaction(Button button){
        bAm.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        bPm.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMyBlack));
        button.setBackgroundResource(R.drawable.light_green_button_shape);
    }

    private String getDayName(int dayOfWeekNumber){
        String dayOfWeekName = "";
        switch(dayOfWeekNumber){
            case 1: dayOfWeekName = DayOfWeek.MONDAY.toString();
                break;
            case 2: dayOfWeekName = DayOfWeek.TUESDAY.toString();
                break;
            case 3: dayOfWeekName = DayOfWeek.WENSDAY.toString();
                break;
            case 4: dayOfWeekName = DayOfWeek.THURSDAY.toString();
                break;
            case 5: dayOfWeekName = DayOfWeek.FRIDAY.toString();
                break;
            case 6: dayOfWeekName = DayOfWeek.SATURDAY.toString();
                break;
            case 7: dayOfWeekName = DayOfWeek.SUNDAY.toString();
                break;
        }
        return dayOfWeekName;
    }
}



