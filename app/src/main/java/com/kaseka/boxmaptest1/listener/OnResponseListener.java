package com.kaseka.boxmaptest1.listener;


import org.json.JSONObject;

public interface OnResponseListener {

    void onSuccess(JSONObject response);

    void onFailure();
}
