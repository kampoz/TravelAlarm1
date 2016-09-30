package com.kaseka.boxmaptest1;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import static android.R.attr.x;
import static android.R.attr.y;

public class AlarmClockActivity extends AppCompatActivity {

    private TimePicker timePickerSetArriveTime;
    private Calendar calendar;
    private String format = "";
    //Resources system;
    private ImageView ivHourHand;
    private ImageView ivMinuteHand;
    private RelativeLayout rlClockParent;
    private View vClockCenterCircle;
    private Point centerPt, targetPt;
    private boolean isMinuteHand = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        ivHourHand = (ImageView) findViewById(R.id.ivHourHand);
        ivHourHand.setAlpha(0.5f);
        ivMinuteHand = (ImageView) findViewById(R.id.ivMinuteHand);
        ivHourHand.setAlpha(0.5f);
        rlClockParent = (RelativeLayout) findViewById(R.id.rlClockParent);
        vClockCenterCircle = findViewById(R.id.vClockCenterCircle);

        ViewTreeObserver vto = rlClockParent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlClockParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Rect myViewRect = new Rect();
                vClockCenterCircle.getGlobalVisibleRect(myViewRect);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = (int)vClockCenterCircle.getLeft() + (vClockCenterCircle.getLayoutParams().width / 2);
                Log.d("params.leftMargin", String.valueOf(params.leftMargin));
                params.topMargin = (int)vClockCenterCircle.getTop() + (vClockCenterCircle.getLayoutParams().height / 2) - (ivHourHand.getHeight()/2);
                ivHourHand.setLayoutParams(params);
                ivMinuteHand.setLayoutParams(params);

                ivHourHand.setPivotX(0);
                ivMinuteHand.setPivotX(0);
                ivHourHand.setPivotY(ivHourHand.getHeight()/2);
                ivMinuteHand.setPivotY(ivHourHand.getHeight()/2);
                //ivHourHand.setRotation(30);
            }
        });

        ivHourHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHourHand.setAlpha(1.0f);
                rlClockParent.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int middleX = (int)vClockCenterCircle.getLeft() + (vClockCenterCircle.getLayoutParams().width / 2);
                        int middleY = (int)vClockCenterCircle.getTop() + (vClockCenterCircle.getLayoutParams().height / 2) - (ivHourHand.getHeight()/2);
                        Log.d("Point middleX", String.valueOf(middleX));
                        Log.d("Point middleY", String.valueOf(middleY));
                        int eventX = (int)event.getX();
                        int eventY = (int)event.getY();
                        Log.d("Point touchX", String.valueOf(eventX));
                        Log.d("Point touchY", String.valueOf(eventY));

                        centerPt = new Point(middleX, middleY);
                        targetPt = new Point(eventX, eventY);
                        float angle = (float)AlarmClockActivity.calcRotationAngleInDegrees(centerPt,targetPt);
                        ivHourHand.setRotation(angle);
                        return true;
                    }
                });
            }
        });
    }

    public static double calcRotationAngleInDegrees(Point centerPt, Point targetPt)
    {
        double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);
        theta += Math.PI/2.0;
        double angle = Math.toDegrees(theta);

        if (angle < 0) {
            angle += 360;
        }
        Log.d("Angle is", String.valueOf(angle));
        return angle-90;
    }

}
