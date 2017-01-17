package com.kampoz.travelalarm.listener;


import org.json.JSONObject;

public interface OnResponseListener {

    void onSuccess(JSONObject response);

    void onFailure();
}
