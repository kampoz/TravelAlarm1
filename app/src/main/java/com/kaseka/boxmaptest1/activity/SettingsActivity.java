package com.kaseka.boxmaptest1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kaseka.boxmaptest1.R;
import com.kaseka.boxmaptest1.data.realm.AlarmRingRealm;

import io.realm.Realm;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioButton rbSound1,rbSound2,rbSound3;
    private ImageButton ibPlaySound1, ibPlaySound2, ibPlaySound3;
    private Button bSetAlarmSound;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean playSound1 = false;
    private boolean playSound2 = false;
    private boolean playSound3 = false;
    private boolean playSound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.title_activity_settings);
        setSupportActionBar(toolbar);

        rbSound1 = (RadioButton)findViewById(R.id.rbSound1);
        rbSound2 = (RadioButton)findViewById(R.id.rbSound2);
        rbSound3 = (RadioButton)findViewById(R.id.rbSound3);

        ibPlaySound1 = (ImageButton) findViewById(R.id.ibPlaySound1);
        ibPlaySound2 = (ImageButton) findViewById(R.id.ibPlaySound2);
        ibPlaySound3 = (ImageButton) findViewById(R.id.ibPlaySound3);

        bSetAlarmSound = (Button) findViewById(R.id.bSetAlarmSound);

        rbSound1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSound1.setChecked(true);
                rbSound2.setChecked(false);
                rbSound3.setChecked(false);
            }
        });

        rbSound2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSound1.setChecked(false);
                rbSound2.setChecked(true);
                rbSound3.setChecked(false);
            }
        });

        rbSound3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSound1.setChecked(false);
                rbSound2.setChecked(false);
                rbSound3.setChecked(true);
            }
        });

        ibPlaySound1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playSound1){
                    playSound1 = true;
                    soundButtonsReaction(ibPlaySound1);
                    mediaPlayer.stop();
                    mediaPlayer = MediaPlayer.create(SettingsActivity.this, R.raw.sound1);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();

                }else{
                    mediaPlayer.stop();
                    playSound1 = false;
                    setColorFiltersToNull();
                }
            }
        });

        ibPlaySound2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playSound2){
                    playSound2 = true;
                    soundButtonsReaction(ibPlaySound2);
                    mediaPlayer.stop();
                    mediaPlayer = MediaPlayer.create(SettingsActivity.this, R.raw.sound2);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();

                }else{
                    mediaPlayer.stop();
                    playSound2 = false;
                    setColorFiltersToNull();
                }
            }
        });

        ibPlaySound3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playSound3){
                    playSound3 = true;
                    soundButtonsReaction(ibPlaySound3);
                    mediaPlayer.stop();
                    mediaPlayer = MediaPlayer.create(SettingsActivity.this, R.raw.sound3);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();

                }else{
                    mediaPlayer.stop();
                    playSound3 = false;
                    setColorFiltersToNull();
                }

            }
        });

        bSetAlarmSound.setOnClickListener(new View.OnClickListener() {
            int soundId;
            Realm realm = Realm.getDefaultInstance();
            AlarmRingRealm alarmRingRealm;
            //alarmRingRealm.setId(1);

            @Override
            public void onClick(View v) {
                if(rbSound1.isChecked()){
                    Toast.makeText(SettingsActivity.this, "Sound 1 set", Toast.LENGTH_LONG).show();
                }
                else if(rbSound2.isChecked()){
                    Toast.makeText(SettingsActivity.this, "Sound 2 set", Toast.LENGTH_LONG).show();
                }
                else if(rbSound3.isChecked()){
                    Toast.makeText(SettingsActivity.this, "Sound 3 set", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SettingsActivity.this, "No sound selected", Toast.LENGTH_LONG).show();
                }


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
        if (id == R.id.action_add_alarm) {
            Intent startMainActivityIntent = new Intent(this, MainActivity.class);
            this.startActivity(startMainActivityIntent);
            finish();
        }

        if (id == R.id.action_show_alarms_list) {
            Intent startAlarmsListActivityIntent = new Intent(this, AlarmsListActivity.class);
            this.startActivity(startAlarmsListActivityIntent);
            this.finish();
        }

        if (id == R.id.action_setting) {

        }

        if (id == R.id.action_about) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.author_data);
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

    private void soundButtonsReaction(ImageButton imageButton) {
            ibPlaySound1.setColorFilter(null);
            ibPlaySound2.setColorFilter(null);
            ibPlaySound3.setColorFilter(null);
            imageButton.setColorFilter(getResources().getColor(R.color.colorMyLightGreen));
        }

    private void setColorFiltersToNull(){
        ibPlaySound1.setColorFilter(null);
        ibPlaySound2.setColorFilter(null);
        ibPlaySound3.setColorFilter(null);

    }
}
