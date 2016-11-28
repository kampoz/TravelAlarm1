package com.kaseka.boxmaptest1.networking;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context mContext){
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public static VolleySingleton getInstance(Context mContext){
        if(mInstance == null){
            mInstance = new VolleySingleton(mContext);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        return this.mRequestQueue;
    }
}