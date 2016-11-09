package com.kaseka.boxmaptest1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;



public class MyRelativeView extends FrameLayout {

    float mDensityScale;

    public MyRelativeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public MyRelativeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MyRelativeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyle)
    {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mDensityScale = dm.density;
    }

    private float pix(float dp)
    {
        return dp * mDensityScale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.GRAY);
//        paint.setStrokeWidth(5);
//
//        RectF oval1 = new RectF(pix(20), pix(20), pix(300), pix(300));
//        canvas.drawOval(oval1, paint);
//
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.BLUE);
//        RectF oval2 = new RectF(pix(30), pix(30), pix(290), pix(290));
//        canvas.drawOval(oval2, paint);

    }
}
