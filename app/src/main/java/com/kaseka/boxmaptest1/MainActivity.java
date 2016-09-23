package com.kaseka.boxmaptest1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.mapbox.geocoder.MapboxGeocoder;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;
import com.mapbox.services.android.geocoder.ui.GeocoderAutoCompleteView;
import com.mapbox.geocoder.android.AndroidGeocoder;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.PolylineUtils;
import com.mapbox.services.geocoding.v5.GeocodingCriteria;
//+import com.mapbox.services.geocoding.v5.models.GeocodingFeature;
import com.mapbox.services.geocoding.v5.models.CarmenFeature;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCFa5n3POS1VSsNgn8NKORx8pGfLSTYBGU";

    private ArrayList<LatLng> responsePoints = new ArrayList<>();

    private MapView mapView;
    private MapboxMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.accessToken));
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapview);
        //mapView.setStyleUrl(Style.MAPBOX_STREETS);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                // Customize map with markers, polylines, etc.
                map = mapboxMap;

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(51.248975, 22.551762)) // Sets the new camera position
                        .zoom(12) // Sets the zoom
                        .bearing(0) // Rotate the camera
                        .tilt(0) // Set the camera tilt
                        .build(); // Creates a CameraPosition from the builder

                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 7000);

//                map.addPolyline(new PolylineOptions()
//                        .add(responsePoints.toArray(new LatLng[responsePoints.size()]))
//                        .color(Color.parseColor("#ff0000"))
//                        .alpha(1.0f)
//                        .width(10));

                drawSimplify(responsePoints);

//                MarkerViewOptions marker = new MarkerViewOptions()
//                        .position(new LatLng(51.248975, 22.551762));
//                marker.title("LUBLIN!!!");
                //mapboxMap.addMarker(marker);
                //mapboxMap.setPadding(20,20,20,20);
            }
        });





        PlaceAutocompleteFragment fromAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.from_autocomplete_fragment);

        fromAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "miejsce wyjazdu: " + place.getName());
                Log.i("TAG", "wspolrzedne: " + place.getLatLng());
                MarkerViewOptions marker = new MarkerViewOptions()
                        .position(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
                marker.title(place.getName().toString());
                map.addMarker(marker);

                setCameraPosition(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });


        PlaceAutocompleteFragment toAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.to_autocomplete_fragment);
        toAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("TAG", "miejsce docelowe: " + place.getName());
                Log.i("TAG", "wspolrzedne: " + place.getLatLng());
                MarkerViewOptions marker = new MarkerViewOptions()
                        .position(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
                marker.title(place.getName().toString());
                map.addMarker(marker);

                setCameraPosition(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        BaseRequest baseRequest = new BaseRequest(this, "https://maps.googleapis.com/maps/api/" +
                "directions/json?origin=place_id:ChIJ8b5DJgJaIkcRqtOAYilxmD4&destination=place_id:" +
                "ChIJncLRe9ZZIkcRtBg-8THvidU&key=AIzaSyCFa5n3POS1VSsNgn8NKORx8pGfLSTYBGU");
        baseRequest.setOnResponseListener(new OnResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("Wykonana metoda","onSuccess");
                responsePoints = decodePoly(Parser.parseWayPoints(response));//= Parser.parseDirections(response);




            }

            @Override
            public void onFailure() {
                Log.d("INTERFEJS","ERROR");
            }
        });
        baseRequest.execute();

    }

    private ArrayList<LatLng> decodePoly(String encoded) {

        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void drawSimplify(ArrayList<LatLng> points) {

        Position[] before = new Position[points.size()];
        for (int i = 0; i < points.size(); i++) before[i] = Position.fromCoordinates(points.get(i).getLongitude(), points.get(i).getLatitude());

        Position[] after = PolylineUtils.simplify(before, 0.001);

        LatLng[] result = new LatLng[after.length];
        for (int i = 0; i < after.length; i++)
            result[i] = new LatLng(after[i].getLatitude(), after[i].getLongitude());

        map.addPolyline(new PolylineOptions()
                .add(result)
                .color(Color.parseColor("#3bb2d0"))
                .width(4));

    }

    @Override
    public void onPause()  {
        super.onPause();
        mapView.onPause();
    }

    /**
     * Called when the activity will start interacting with the use
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * The final call you receive before your activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void setCameraPosition(double latitude, double longitude){
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)) // Sets the new camera position
                .zoom(10) // Sets the zoom
                .bearing(90) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 7000);
    }



}
