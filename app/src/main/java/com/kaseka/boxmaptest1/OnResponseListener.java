package com.kaseka.boxmaptest1;


import org.json.JSONObject;

public interface OnResponseListener {

    void onSuccess(JSONObject response);

    void onFailure();
}
