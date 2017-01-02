package com.kaseka.boxmaptest1.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kaseka.boxmaptest1.R;

public class AlarmActivity extends AppCompatActivity {

    ImageButton bCloseAlarm;
    MediaPlayer mMediaPlayer;
    View circleWaveView;
    Animation pulse1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        //getSupportActionBar().hide();

        bCloseAlarm = (ImageButton) findViewById(R.id.bCloseAlarm);
        bCloseAlarm.setFocusable(true);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.elephant1);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        //mMediaPlayer.start();

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
        bCloseAlarm = (ImageButton) findViewById(R.id.bCloseAlarm);
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
                finish();

            }
        });

    }




}

