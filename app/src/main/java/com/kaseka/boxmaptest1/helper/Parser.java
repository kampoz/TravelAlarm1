package com.kaseka.boxmaptest1.helper;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.kaseka.boxmaptest1.application.MyApplication;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Parser {

    static Context context;

    public static ArrayList<LatLng> parseDirections(JSONObject response){

        ArrayList<LatLng> points = new ArrayList<>();

        JSONArray routes = response.optJSONArray("routes");
        JSONArray legs = routes.optJSONObject(0).optJSONArray("legs");
        JSONArray steps = legs.optJSONObject(0).optJSONArray("steps");

        for(int i=0; i<steps.length();i++){
            JSONObject step = steps.optJSONObject(i);
            JSONObject endLocation = step.optJSONObject("end_location");
            JSONObject startLocation = step.optJSONObject("start_location");
            points.add(new LatLng(endLocation.optDouble("lat"), endLocation.optDouble("lng")));
            points.add(new LatLng(startLocation.optDouble("lat"), startLocation.optDouble("lng")));
        }
        for(int i=0; i<points.size();i++){
            Log.d("Points "+i, points.get(i).toString());
        }
        return new ArrayList<LatLng>(new LinkedHashSet<LatLng>(points));
    }


    public static String parseRoutePoints(JSONObject response){
        ArrayList<LatLng> points = new ArrayList<>();
        JSONArray routes = response.optJSONArray("routes");
        if (routes.length()>0){
            JSONObject overviewPolyline = routes.optJSONObject(0).optJSONObject("overview_polyline");
            String wayPoints = overviewPolyline.optString("points");
            return wayPoints;
        }else{
            Toast.makeText(MyApplication.getAppContext(), "No data", Toast.LENGTH_SHORT).show();
            return "";
        }

    }


    public static String parseWholeRouteTime(JSONObject response){
        ArrayList<LatLng> points = new ArrayList<>();
        JSONArray routes = response.optJSONArray("routes");
        JSONArray legs = routes.optJSONObject(0).optJSONArray("legs");
        JSONObject duration = legs.optJSONObject(0).optJSONObject("duration");
        String wholeRouteTime = duration.optString("text");
        return wholeRouteTime;
    }

    public static int parseRouteTimeInSekonds(JSONObject response){
        ArrayList<LatLng> points = new ArrayList<>();
        JSONArray routes = response.optJSONArray("routes");
        JSONArray legs = routes.optJSONObject(0).optJSONArray("legs");
        JSONObject duration = legs.optJSONObject(0).optJSONObject("duration");
        int RouteTimeInSekonds = duration.optInt("value");
        return RouteTimeInSekonds;
    }
}
