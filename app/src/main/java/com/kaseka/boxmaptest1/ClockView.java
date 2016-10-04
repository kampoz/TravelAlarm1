package com.kaseka.boxmaptest1;


import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ClockView extends RelativeLayout implements View.OnClickListener  {

    private ImageView ivHourHand;
    private ImageView ivMinuteHand;
    private View vClockCenterCircle;
    private ImageView activeHand = null;
    private ImageView inactiveHand = null;
    private Point centerPt, targetPt;
    private OnClockChangeListener onClockChangeListener;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.clock, this);

        this.ivHourHand = (ImageView)findViewById(R.id.ivHourHand);
        this.ivMinuteHand = (ImageView)findViewById(R.id.ivMinuteHand);
        this.vClockCenterCircle = (View)findViewById(R.id.vClockCenterCircle);

        ivHourHand.setOnClickListener(this);
        ivMinuteHand.setOnClickListener(this);

        ivHourHand.setAlpha(0.7f);
        ivMinuteHand.setAlpha(0.7f);
        //this.setBackground(getContext().getDrawable());

        setHandsStartPosition();

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int middleX = vClockCenterCircle.getLeft() + (vClockCenterCircle.getLayoutParams().width / 2);
                int middleY = vClockCenterCircle.getTop() + (vClockCenterCircle.getLayoutParams().height / 2) - (ivHourHand.getHeight()/2);
                Log.d("Point middleX", String.valueOf(middleX));
                Log.d("Point middleY", String.valueOf(middleY));
                int eventX = (int)event.getX();
                int eventY = (int)event.getY();
                Log.d("Point touchX", String.valueOf(eventX));
                Log.d("Point touchY", String.valueOf(eventY));
                centerPt = new Point(middleX, middleY);
                targetPt = new Point(eventX, eventY);
                float angle = (float)ClockView.calcRotationAngleInDegrees(centerPt,targetPt);
                if (activeHand != null) {
                    activeHand.setRotation(angle-90);
                    onClockChangeListener.onTimeChange(getHour() +":" + getMinute());
                }
                return true;
            }
        });

    }

    public void setOnClockChangeListener(OnClockChangeListener onClockChangeListener) {
        this.onClockChangeListener = onClockChangeListener;
    }

    public float getMinuteAngle(){
        float minuteAngle = ivMinuteHand.getRotation();
        Log.d("godz getrota()", String.valueOf(minuteAngle));
        if (minuteAngle<0){
            minuteAngle = 360+minuteAngle;
        }
        if (minuteAngle>360){
            minuteAngle = minuteAngle-360;
        }

        if (minuteAngle > 270 && minuteAngle <= 360) {
            return minuteAngle - 270;
        } else {
            return minuteAngle + 90;
        }
    }

    public float getHourAngle(){
        float hourAngle = ivHourHand.getRotation();
        if (hourAngle<0){
            hourAngle = 360+hourAngle;
        }
        if (hourAngle>360){
            hourAngle = hourAngle-360;
        }

        if (hourAngle > 270 && hourAngle < 360) {
            return hourAngle - 270;
        } else {
            return hourAngle + 90;
        }
    }

    public String getMinute(){
        return String.valueOf((int)(getMinuteAngle()*60/360));
    }

    public String getHour(){
        return String.valueOf((int)(getHourAngle()*12/360));
    }

    private void setHandsStartPosition(){
        ViewTreeObserver vto = this.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Rect myViewRect = new Rect();
                vClockCenterCircle.getGlobalVisibleRect(myViewRect);

                ivHourHand.setX(vClockCenterCircle.getX() + (vClockCenterCircle.getWidth() / 2));
                ivHourHand.setY(vClockCenterCircle.getY() + (vClockCenterCircle.getHeight() / 2) - ivHourHand.getHeight() /2);
                ivHourHand.setPivotX(0);
                ivHourHand.setPivotY(ivHourHand.getHeight()/2);
                ivMinuteHand.setX(vClockCenterCircle.getX() + (vClockCenterCircle.getWidth() / 2));
                ivMinuteHand.setY(vClockCenterCircle.getY() + (vClockCenterCircle.getHeight() / 2) - ivMinuteHand.getHeight() /2);
                ivMinuteHand.setPivotX(0);
                ivMinuteHand.setPivotY(ivMinuteHand.getHeight()/2);
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
        return angle;
    }

    @Override
    public void onClick(View v) {
        activeHand = (ImageView) v;
        inactiveHand = activeHand == ivMinuteHand ? ivHourHand : ivMinuteHand;
        activeHand.setAlpha(1.0f);
        inactiveHand.setAlpha(0.7f);
    }
}
