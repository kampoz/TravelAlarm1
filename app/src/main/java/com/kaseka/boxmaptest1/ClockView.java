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
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ClockView extends RelativeLayout implements View.OnClickListener  {

    private ClockHandView ivHourHand;
    private ClockHandView ivMinuteHand;
    private ClockHandView ivHourLine1;
    private ClockHandView ivHourLine2;
    private ClockHandView ivHourLine3;
    private ClockHandView ivHourLine4;
    private ClockHandView ivHourLine5;
    private ClockHandView ivHourLine6;
    private ClockHandView ivHourLine7;
    private ClockHandView ivHourLine8;
    private ClockHandView ivHourLine9;
    private ClockHandView ivHourLine10;
    private ClockHandView ivHourLine11;
    private ClockHandView ivHourLine12;
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

        this.ivHourLine1 = (ClockHandView)findViewById(R.id.ivHourLine1);
        this.ivHourLine2 = (ClockHandView)findViewById(R.id.ivHourLine2);
        this.ivHourLine3 = (ClockHandView)findViewById(R.id.ivHourLine3);
        this.ivHourLine4 = (ClockHandView)findViewById(R.id.ivHourLine4);
        this.ivHourLine5 = (ClockHandView)findViewById(R.id.ivHourLine5);
        this.ivHourLine6 = (ClockHandView)findViewById(R.id.ivHourLine6);
        this.ivHourLine7 = (ClockHandView)findViewById(R.id.ivHourLine7);
        this.ivHourLine8 = (ClockHandView)findViewById(R.id.ivHourLine8);
        this.ivHourLine9 = (ClockHandView)findViewById(R.id.ivHourLine9);
        this.ivHourLine10 = (ClockHandView)findViewById(R.id.ivHourLine10);
        this.ivHourLine11 = (ClockHandView)findViewById(R.id.ivHourLine11);
        this.ivHourLine12 = (ClockHandView)findViewById(R.id.ivHourLine12);

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

                int hourHandHeight = (int)((ClockView.this.getWidth()/2 /*- (20 / getContext().getResources().getDisplayMetrics().density)*/) *0.6 );
                ivHourHand.getLayoutParams().height = hourHandHeight;
                ivHourHand.getLayoutParams().width = 30;

                ivHourHand.setX(vClockCenterCircle.getX() + vClockCenterCircle.getWidth()/2 - ivHourHand.getLayoutParams().width/2);
                ivHourHand.setY(vClockCenterCircle.getY() + vClockCenterCircle.getHeight()/2 - ivHourHand.getLayoutParams().height);
                ivHourHand.setPivotX(ivHourHand.getLayoutParams().width/2);
                ivHourHand.setPivotY(ivHourHand.getLayoutParams().height);

                int minuteHandHeight = (int)((ClockView.this.getWidth()/2 /*- (20 / getContext().getResources().getDisplayMetrics().density)*/) * 0.8);
                ivMinuteHand.getLayoutParams().height = minuteHandHeight;
                ivMinuteHand.getLayoutParams().width = 20;

                ivMinuteHand.setX(vClockCenterCircle.getX() + vClockCenterCircle.getWidth()/2 - ivMinuteHand.getLayoutParams().width/2);
                ivMinuteHand.setY(vClockCenterCircle.getY() + vClockCenterCircle.getHeight()/2 - ivMinuteHand.getLayoutParams().height);
                ivMinuteHand.setPivotX(ivMinuteHand.getLayoutParams().width/2);
                ivMinuteHand.setPivotY(ivMinuteHand.getLayoutParams().height);

                int parentCenterX = (int)(ClockView.this.getX() + ClockView.this.getWidth()/2);
                int parentCenterY = (int)(ClockView.this.getY() + ClockView.this.getHeight()/2);


                Log.d("ClockView getWidth: ",String.valueOf(ClockView.this.getWidth()));
                Log.d("ClockView getHeight ",String.valueOf(ClockView.this.getHeight()));

                Log.d("ClockViewX: ",String.valueOf(ClockView.this.getX()));
                Log.d("ClockViewY: ",String.valueOf(ClockView.this.getY()));

                Log.d("parentCenterX: ",String.valueOf(parentCenterX));
                Log.d("parentCenterY: ",String.valueOf(parentCenterY));

                Log.d("CircleCenterX: ",String.valueOf(vClockCenterCircle.getX()));
                Log.d("CircleCenterY: ",String.valueOf(vClockCenterCircle.getY()));

                int quaterLine1Height = ClockView.this.getWidth()/8;
                int ordinaryLineHeight = ClockView.this.getWidth()/12;

//                ivHourLine12.getLayoutParams().height = quaterLine1Height;
//                ivHourLine12.getLayoutParams().width = 5;
//                ivHourLine12.setX(vClockCenterCircle.getX() + vClockCenterCircle.getWidth()/2 - ivHourLine12.getLayoutParams().width/2);
//                int clockFacemargin = ClockView.this.getHeight()/20;
//                ivHourLine12.setY(clockFacemargin);
//                ivHourLine12.setPivotX(ivHourLine3.getLayoutParams().width/2);
//                ivHourLine12.setPivotY(ivHourLine3.getLayoutParams().height+(ClockView.this.getHeight()/2-ivHourLine3.getLayoutParams().height)-clockFacemargin);
//                ivHourLine12.setRotation(0);
//
//
//                ivHourLine1.getLayoutParams().height = ordinaryLineHeight;
//                ivHourLine1.getLayoutParams().width = 2;
//                ivHourLine1.setX(vClockCenterCircle.getX() + vClockCenterCircle.getWidth()/2 - ivHourLine12.getLayoutParams().width/2);
//                ivHourLine1.setY(clockFacemargin);
//                ivHourLine1.setPivotX(ivHourLine3.getLayoutParams().width/2);
//                ivHourLine1.setPivotY(ivHourLine3.getLayoutParams().height+(ClockView.this.getHeight()/2-ivHourLine3.getLayoutParams().height)-clockFacemargin);
//                ivHourLine1.setRotation(30);
//
//                ivHourLine2.getLayoutParams().height = ordinaryLineHeight;
//                ivHourLine2.getLayoutParams().width = 2;
//                ivHourLine2.setX(vClockCenterCircle.getX() + vClockCenterCircle.getWidth()/2 - ivHourLine12.getLayoutParams().width/2);
//                ivHourLine2.setY(clockFacemargin);
//                ivHourLine2.setPivotX(ivHourLine3.getLayoutParams().width/2);
//                ivHourLine2.setPivotY(ivHourLine3.getLayoutParams().height+(ClockView.this.getHeight()/2-ivHourLine3.getLayoutParams().height)-clockFacemargin);
//                ivHourLine2.setRotation(60);


                drawQuaterHourLine(ivHourLine3,3);
                drawQuaterHourLine(ivHourLine6,6);
                drawQuaterHourLine(ivHourLine9,9);
                drawQuaterHourLine(ivHourLine12,0);

                drawOrdinaryHourLine(ivHourLine1,1);
                drawOrdinaryHourLine(ivHourLine2,2);
                drawOrdinaryHourLine(ivHourLine4,4);
                drawOrdinaryHourLine(ivHourLine5,5);
                drawOrdinaryHourLine(ivHourLine7,7);
                drawOrdinaryHourLine(ivHourLine8,8);
                drawOrdinaryHourLine(ivHourLine10,10);
                drawOrdinaryHourLine(ivHourLine11,11);


            }
        });
    }

    private void drawQuaterHourLine(ClockHandView view, int hour){
        int angle = hour*30;
        int clockFacemargin = ClockView.this.getHeight()/20;
        view.getLayoutParams().height = ClockView.this.getWidth()/12;
        view.getLayoutParams().width = ClockView.this.getWidth()/50;
        view.setX(vClockCenterCircle.getX() + vClockCenterCircle.getWidth()/2 - ivHourLine12.getLayoutParams().width/2);
        view.setY(clockFacemargin);
        view.setPivotX(ivHourLine12.getLayoutParams().width/2);
        view.setPivotY(ClockView.this.getHeight()/2-clockFacemargin);
        view.setRotation(angle);
    };

    private void drawOrdinaryHourLine(ClockHandView view, int hour){
        int angle = hour*30;
        int clockFacemargin = ClockView.this.getHeight()/20;
        view.getLayoutParams().height = ClockView.this.getWidth()/12;;
        view.getLayoutParams().width = 3;
        view.setX(vClockCenterCircle.getX() + vClockCenterCircle.getWidth()/2 - ivHourLine12.getLayoutParams().width/2);
        view.setY(clockFacemargin);
        view.setPivotX(ivHourLine12.getLayoutParams().width/2);
        view.setPivotY(ClockView.this.getHeight()/2-clockFacemargin);
        view.setRotation(angle);

    };

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
