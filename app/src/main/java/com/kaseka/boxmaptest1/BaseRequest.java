package com.kaseka.boxmaptest1;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseRequest {

    private Context context;
    OnResponseListener onResponseListener;

    public BaseRequest(Context context) {
        this.context = context;
    }

    public void execute(){
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, getEndpoint() + "&key=" + context.getString(R.string.googleToken),
        //final StringRequest stringRequest = new StringRequest(Request.Method.GET, getEndpoint()+"&key=AIzaSyCFa5n3POS1VSsNgn8NKORx8pGfLSTYBGU",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Request Response: ", response);
                        try {
                            onResponseListener.onSuccess(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(context, "Błąd requesta", Toast.LENGTH_LONG);
                toast.show();
                onResponseListener.onFailure();
            }
        });
        VolleySingleton.getInstance(context).getRequestQueue().add(stringRequest);
    }

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public String getEndpoint(){
        return "";
    }
}
