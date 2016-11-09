package com.kaseka.boxmaptest1;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.zip.Inflater;

public class AlarmActivity extends AppCompatActivity {

    ImageButton bCloseAlarm;
    MediaPlayer mMediaPlayer;
    View circleWaveView;
    Activity_Animation1_Layout animationLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animationLayout = new Activity_Animation1_Layout(this);

        //setContentView(R.layout.activity_alarm);
        setContentView(R.layout.activity_alarm);

        bCloseAlarm = (ImageButton) findViewById(R.id.bCloseAlarm);
        bCloseAlarm.setFocusable(true);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.elephant1);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        //mMediaPlayer.start();

        addListenerOnButton();

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
                animationLayout.animationStop();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //animationLayout.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationLayout.resume();
    }


}

