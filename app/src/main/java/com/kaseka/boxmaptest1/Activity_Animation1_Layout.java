package com.kaseka.boxmaptest1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;



public class Activity_Animation1_Layout extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    Thread thread = null;
    boolean canDraw = false;
    Paint blueBrush, redBrush;

    Bitmap backgroundCheck;
    Canvas canvas;
    SurfaceHolder surfaceHolder;

    float cx, cy, radius;

    public Activity_Animation1_Layout(Context context) {
        super(context);
        init();
    }

    public Activity_Animation1_Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Activity_Animation1_Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        //setBackgroundResource(R.drawable.circle);

        blueBrush = new Paint();
        blueBrush.setColor(Color.BLUE);
        blueBrush.setStyle(Paint.Style.STROKE);
        blueBrush.setStrokeWidth(10);

        redBrush = new Paint();
        redBrush.setColor(Color.rgb(153,255,255));
        redBrush.setStyle(Paint.Style.STROKE);
        redBrush.setStrokeWidth(2);

        cx = 200;
        cy = 200;

        radius = 150;
    }

    @Override
    public void run() {
        while (canDraw){

            if(!surfaceHolder.getSurface().isValid()){
                continue;
            }
            canvas = surfaceHolder.lockCanvas();
            cx = canvas.getWidth()/2;
            cy = canvas.getHeight()/2;

            canvas.drawCircle(cx,cy,radius,redBrush);
            surfaceHolder.unlockCanvasAndPost(canvas);
            try {
                thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            radius += 20;
        }
    }

    public void pause() {
        canDraw = false;
        while(true){
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        thread = null;
    }

    public void resume() {
        canDraw = true;
        thread = new Thread(this);
        thread.start();
    }

    public void animationStop(){
        canDraw = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        resume();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}
