package com.kampoz.travelalarm.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class ClockHandView extends View {



    public ClockHandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClockHandView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockHandView(Context context) {
        super(context);
    }

    @Override
    public float getRotation() {
        return ( super.getRotation() +360) % 360;
    }
}
