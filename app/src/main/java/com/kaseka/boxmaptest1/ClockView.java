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
                    activeHand.setRotation(angle);
                }
                return true;
            }
        });

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
                ivMinuteHand.setRotation(300.0f);
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

    @Override
    public void onClick(View v) {
        activeHand = (ImageView) v;
        inactiveHand = activeHand == ivMinuteHand ? ivHourHand : ivMinuteHand;
        activeHand.setAlpha(1.0f);
        inactiveHand.setAlpha(0.7f);
    }

}
