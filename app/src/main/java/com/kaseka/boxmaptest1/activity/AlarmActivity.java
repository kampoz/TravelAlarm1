package com.kaseka.boxmaptest1.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.data.realm.AlarmRingRealm;

import io.realm.Realm;

public class AlarmActivity extends AppCompatActivity {

    Button bCloseAlarm;
    MediaPlayer mMediaPlayer;
    View circleWaveView;
    Animation pulse1;
    AlarmRingRealm alarmRingRealm;
    Context context = this;
    TextView tvAlarmLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        //getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        String alarmDayOfweekFromService = extras.getString("DESTINATION");

        tvAlarmLabel = (TextView)findViewById(R.id.tvAlarmLabel);
        tvAlarmLabel.setText("Time to: "+alarmDayOfweekFromService);
        bCloseAlarm = (Button) findViewById(R.id.bCloseAlarm);
        bCloseAlarm.setFocusable(true);


        Realm realm = Realm.getDefaultInstance();
        int soundId = realm.where(AlarmRingRealm.class).equalTo("id", 1).findFirst().getSoundId();
        Log.d("currentSound: ", String.valueOf(soundId));

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, soundId);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        addListenerOnButton();


        circleWaveView = findViewById(R.id.circle_wave_view1);
        ViewTreeObserver vto = circleWaveView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pulse1 = AnimationUtils.loadAnimation(AlarmActivity.this, R.anim.anim1);
                circleWaveView.startAnimation(pulse1);
            }
        });
    }

        public void addListenerOnButton(){
        //bCloseAlarm = (ImageButton) findViewById(R.id.bCloseAlarm);
        bCloseAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AlarmActivity.this, "Alarm off",
                        Toast.LENGTH_SHORT).show();
                //bCloseAlarm.setSelected(true);
                //bCloseAlarm.setFocusableInTouchMode(false);
                //bCloseAlarm.setFocusable(false);
                mMediaPlayer.stop();
                pulse1.cancel();
                Intent startAlarmsListActivityIntent = new Intent(context, AlarmsListActivity.class);
                context.startActivity(startAlarmsListActivityIntent);
                finish();

            }
        });

    }




}

