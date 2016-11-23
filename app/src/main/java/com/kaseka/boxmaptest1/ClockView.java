package com.kaseka.boxmaptest1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ClockView extends RelativeLayout implements View.OnClickListener  {

    private ClockHandView ivHourHand;
    private ClockHandView ivMinuteHand;

    private View vClockCenterCircle;
    private View activeHand = null;
    private View inactiveHand = null;
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

        Drawable drawable = getContext().getResources().getDrawable(R.drawable.hour_hand);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        this.ivHourHand = (ClockHandView)findViewById(R.id.ivHourHand);
        this.ivMinuteHand = (ClockHandView)findViewById(R.id.ivMinuteHand);
        this.vClockCenterCircle = (View)findViewById(R.id.vClockCenterCircle);

//        final ViewTreeObserver vto = this.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                ivHourHand.getLayoutParams().height = ClockView.this.getMeasuredHeight()/2;
//            }
//        });

        ivHourHand.setOnClickListener(this);
        ivMinuteHand.setOnClickListener(this);

        ivHourHand.setAlpha(0.7f);
        ivMinuteHand.setAlpha(0.7f);

        setHandsStartPosition();

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int middleX = vClockCenterCircle.getLeft() + (vClockCenterCircle.getLayoutParams().width / 2);
                int middleY = vClockCenterCircle.getTop() + (vClockCenterCircle.getLayoutParams().height / 2);
                int eventX = (int)event.getX();
                int eventY = (int)event.getY();
                Log.d("Point touchX", String.valueOf(eventX));
                Log.d("Point touchY", String.valueOf(eventY));
                centerPt = new Point(middleX, middleY);
                targetPt = new Point(eventX, eventY);
                float angle = (float)ClockView.calcRotationAngleInDegrees(centerPt,targetPt);
                float hourAngle = correctHourAngle(angle);
                float minuteAngle = correctMinuteAngle(angle);
                if (activeHand != null) {
                    if(activeHand == ivMinuteHand) {
                        activeHand.setRotation(minuteAngle);
                    } else {
                        activeHand.setRotation(hourAngle);
                        Log.d("kąt godziny", String.valueOf(hourAngle));
                    }
                    onClockChangeListener.onTimeChange(getHour(ivHourHand.getRotation()).toString() +":" + getMinute(ivMinuteHand.getRotation()).toString());
                }
                return true;
            }
        });

    }

    public View getIvHourHand() {
        return ivHourHand;
    }

    public View getIvMinuteHand() {
        return ivMinuteHand;
    }

    public Integer getMinute(float angle){
        return (int)(angle*60/360);
    }

    public Integer getHour(float angle){
        return (int)(angle*12/360);
    }

    public float correctMinuteAngle(float angle) {
        return getMinute(angle) * 6;
    }

    public float correctHourAngle(float angle) {
        return getHour(angle) * 30;
    }

    public void setOnClockChangeListener(OnClockChangeListener onClockChangeListener) {
        this.onClockChangeListener = onClockChangeListener;
    }


    private void setHandsStartPosition(){
        ViewTreeObserver vto = this.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Rect myViewRect = new Rect();
                vClockCenterCircle.getGlobalVisibleRect(myViewRect);

                int hourHandHeight = (int)((ClockView.this.getWidth()/2 - (20 / getContext().getResources().getDisplayMetrics().density)) * 0.6);
                ivHourHand.getLayoutParams().height = hourHandHeight;
                ivHourHand.getLayoutParams().width = 30;

                ivHourHand.setX(vClockCenterCircle.getX() + vClockCenterCircle.getWidth()/2 - ivHourHand.getLayoutParams().width/2);
                ivHourHand.setY(vClockCenterCircle.getY() + vClockCenterCircle.getHeight()/2 - ivHourHand.getLayoutParams().height);
                ivHourHand.setPivotX(ivHourHand.getLayoutParams().width/2);
                ivHourHand.setPivotY(ivHourHand.getLayoutParams().height);



                int minuteHandHeight = (int)((ClockView.this.getWidth()/2 - (20 / getContext().getResources().getDisplayMetrics().density)) * 0.8);
                ivMinuteHand.getLayoutParams().height = minuteHandHeight;
                ivMinuteHand.getLayoutParams().width = 20;

                ivMinuteHand.setX(vClockCenterCircle.getX() + vClockCenterCircle.getWidth()/2 - ivMinuteHand.getLayoutParams().width/2);
                ivMinuteHand.setY(vClockCenterCircle.getY() + vClockCenterCircle.getHeight()/2 - ivMinuteHand.getLayoutParams().height);
                ivMinuteHand.setPivotX(ivMinuteHand.getLayoutParams().width/2);
                ivMinuteHand.setPivotY(ivMinuteHand.getLayoutParams().height);
            }
        });
    }

    public static double calcRotationAngleInDegrees(Point centerPt, Point targetPt)
    {
        double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);
        theta += Math.PI/2.0;
        double angle = Math.toDegrees(theta);

        angle=(angle+360)%360;

        Log.d("Angle is", String.valueOf(angle));
        return angle;
    }

    @Override
    public void onClick(View v) {
        activeHand = (View) v;
        inactiveHand = activeHand == ivMinuteHand ? ivHourHand : ivMinuteHand;
        activeHand.setAlpha(1.0f);
        inactiveHand.setAlpha(0.7f);

    }
}
