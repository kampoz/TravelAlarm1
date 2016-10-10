package com.kaseka.boxmaptest1;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class ClockHandImageView extends ImageView{



    public ClockHandImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClockHandImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockHandImageView(Context context) {
        super(context);
    }

    @Override
    public float getRotation() {
        return ( super.getRotation() +360) % 360;
    }
}
