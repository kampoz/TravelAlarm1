package com.kaseka.boxmaptest1;

import android.graphics.Color;
import android.util.Log;

import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.PolylineUtils;

import java.util.ArrayList;
import java.util.List;


public class MapBoxHelper {

    private MapboxMap map;

    LatLng firtsPolyPointLatLng;
    LatLng lastPolyPointLatLng;

    public MapBoxHelper(MapboxMap map){
        this.map = map;
    }

    public void drawSimplify(ArrayList<LatLng> points) {

        Position[] before = new Position[points.size()];
        for (int i = 0; i < points.size(); i++)
            before[i] = Position.fromCoordinates(points.get(i).getLongitude(),
                    points.get(i).getLatitude());

        Position[] after = PolylineUtils.simplify(before, 0.001);

        LatLng[] result = new LatLng[after.length];
        for (int i = 0; i < after.length; i++)
            result[i] = new LatLng(after[i].getLatitude(), after[i].getLongitude());

        map.addPolyline(new PolylineOptions()
                .add(result)
                .color(Color.parseColor("#00ff00"))
                .width(6));
    }

    public void drawBeforeSimplify(ArrayList<LatLng> points) {

        LatLng[] pointsArray = new LatLng[points.size()];
        for (int i = 0; i < points.size(); i++)
            pointsArray[i] = new LatLng(points.get(i).getLatitude(), points.get(i).getLongitude());

        map.addPolyline(new PolylineOptions()
                .add(pointsArray)
                .color(Color.parseColor("#ff0000"))
                .alpha(0.65f)
                .width(5));
        String firtsPolyPoint = points.get(0).toString();
        String lastPolyPoint = points.get(points.size()-1).toString();
        firtsPolyPointLatLng = points.get(0);
        lastPolyPointLatLng = points.get(points.size()-1);
        Log.d("PUNKTY POLY", "Pierwszy: "+firtsPolyPoint);
        Log.d("PUNKTY POLY", "Ostatni: "+lastPolyPoint);
    }

    public LatLng getFirtsPolyPointLatLng() {
        return firtsPolyPointLatLng;
    }

    public LatLng getLastPolyPointLatLng() {
        return lastPolyPointLatLng;
    }

    public void fitZoom(LatLng markerPositionFrom, LatLng markerPositionTo){

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(markerPositionFrom)
                .include(markerPositionTo)
                .build();

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
    }
}