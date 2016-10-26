package com.kaseka.boxmaptest1;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//+import com.mapbox.services.geocoding.v5.models.GeocodingFeature;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean debug = true;

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCFa5n3POS1VSsNgn8NKORx8pGfLSTYBGU";

    private ArrayList<LatLng> responsePoints = new ArrayList<>();
    private ScrollView scrollView;
    private MapView mapView;
    private MapboxMap map;
    private MarkerView markerViewFrom;
    private MarkerView markerViewTo;
    private Button bStart;
    private Button bDalej;
    private TextView tvRouteTime;
    private String fromLocationId = "";
    private String toLocationId = "";
    String routeTime;
    int routeTimeInSeconds = 0;
    GetRouteDetailsRequest getRouteDetailsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.accessToken));
        setContentView(R.layout.activity_main);

//        // "https://maps.googleapis.com/maps/api/" +
//        "directions/json?origin=place_id:ChIJ8b5DJgJaIkcRqtOAYilxmD4&destination=place_id:" +
//                "ChIJncLRe9ZZIkcRtBg-8THvidU&key=AIzaSyCFa5n3POS1VSsNgn8NKORx8pGfLSTYBGU"
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        bStart = (Button) findViewById(R.id.bStart);
        bDalej = (Button) findViewById(R.id.bDalej);
        mapView = (MapView) findViewById(R.id.mapview);
        //mapView.setStyleUrl(Style.MAPBOX_STREETS);
        tvRouteTime = (TextView) findViewById(R.id.tvRouteTime);

        //tworzenie mapy
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

                map.addPolyline(new PolylineOptions()
                        .add(responsePoints.toArray(new LatLng[responsePoints.size()]))
                        .color(Color.parseColor("#ff0000"))
                        .alpha(1.0f)
                        .width(10));

            }
        });


        final CustomAutocompleteFragment fromAutocompleteFragment = (CustomAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.from_autocomplete_fragment);
        fromAutocompleteFragment.setHintText("Skąd...");
        fromAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.i("TAG", "miejsce wyjazdu: " + place.getName());
                Log.i("TAG", "wspolrzedne: " + place.getLatLng());
                MarkerViewOptions marker = new MarkerViewOptions()
                        .position(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
                marker.title(place.getName().toString());
                markerViewFrom = map.addMarker(marker);
                Log.d("punkty", "wspolrzedne z: " + place.getLatLng());
                fromLocationId = place.getId();
                setCameraPosition(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i("TAG", "An error occurred: " + status);
            }
        });


        final CustomAutocompleteFragment toAutocompleteFragment = (CustomAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.to_autocomplete_fragment);
        toAutocompleteFragment.setHintText("Dokąd...");
        toAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("TAG", "miejsce docelowe: " + place.getName());
                Log.i("TAG", "wspolrzedne: " + place.getLatLng());
                MarkerViewOptions marker = new MarkerViewOptions()
                        .position(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
                marker.title(place.getName().toString());
                markerViewTo = map.addMarker(marker);
                Log.d("punkty", "wspolrzedne do: " + place.getLatLng());
                toLocationId = place.getId();
                setCameraPosition(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fromLocationId.isEmpty() && !toLocationId.isEmpty() )
                {
                    setRequest();
                    //Parser parser = new Parser();
                    //ArrayList<LatLng> points = parser.parseRoutePoints();

                } else {
                    Toast.makeText(MainActivity.this, "Brak lokalizacji", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bDalej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAlarmClockActivityIntent = new Intent(MainActivity.this, AlarmClockActivity.class);
                startAlarmClockActivityIntent.putExtra("travelTimeInSeconds", routeTimeInSeconds);
                MainActivity.this.startActivity(startAlarmClockActivityIntent);
            }
        });
    }


    private void setRequest(){
        getRouteDetailsRequest = new GetRouteDetailsRequest(this, fromLocationId, toLocationId);
        getRouteDetailsRequest.setOnResponseListener(new OnResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("Wykonana metoda","onSuccess");
                responsePoints = GoogleDirectionsHelper.decodePoly(Parser.parseRoutePoints(response));//= Parser.parseDirections(response);
                routeTime= Parser.parseWholeRouteTime(response);
                routeTimeInSeconds = Parser.parseRouteTimeInSekonds(response);
                //map.clear();
                MapBoxHelper mapBoxHelper = new MapBoxHelper(map);

//                MarkerViewOptions markerFrom = new MarkerViewOptions()
//                        .position(responsePoints.get(0));
//                MarkerViewOptions markerTo = new MarkerViewOptions()
//                        .position(responsePoints.get(responsePoints.size()-1));

                markerViewFrom.setPosition(responsePoints.get(0));
                markerViewFrom.setAnchor(0.5f,1.0f);
                markerViewTo.setPosition(responsePoints.get(responsePoints.size()-1));
                markerViewTo.setAnchor(0.5f,1.0f);
                //mapBoxHelper.drawSimplify(responsePoints, map);
                mapBoxHelper.drawBeforeSimplify(responsePoints);
                tvRouteTime.setText("Czas przejazdu: "+routeTime);

                mapBoxHelper.fitZoom(markerViewFrom.getPosition(),markerViewTo.getPosition());
            }

            @Override
            public void onFailure() {
                Log.d("INTERFEJS","ERROR");
            }
        });
        getRouteDetailsRequest.execute();
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
                .zoom(13) // Sets the zoom
                .bearing(380) // Rotate the camera
                .tilt(90) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 7000);
    }

}
