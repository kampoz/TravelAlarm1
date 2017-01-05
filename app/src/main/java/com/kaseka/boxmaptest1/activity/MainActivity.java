package com.kaseka.boxmaptest1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.kaseka.boxmaptest1.data.realm.AlarmPOJO;
import com.kaseka.boxmaptest1.data.realm.LatLngRealm;
import com.kaseka.boxmaptest1.fragment.CustomAutocompleteFragment;
import com.kaseka.boxmaptest1.helper.Cache;
import com.kaseka.boxmaptest1.networking.GetRouteDetailsRequest;
import com.kaseka.boxmaptest1.helper.GoogleDirectionsHelper;
import com.kaseka.boxmaptest1.global.GoogleTransportMode;
import com.kaseka.boxmaptest1.helper.MapBoxHelper;
import com.kaseka.boxmaptest1.listener.OnResponseListener;
import com.kaseka.boxmaptest1.helper.Parser;
import com.kaseka.boxmaptest1.R;
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

import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//+import com.mapbox.services.geocoding.v5.models.GeocodingFeature;

import org.joda.time.DateTime;
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
    private Button bSetRoute;
    private Button bNext;
    private Button bClean;
    private TextView tvRouteTime;
    private String fromLocationId = "";
    private String toLocationId = "";
    String routeTimeLabel;
    int routeTimeInSeconds = 0;
    GetRouteDetailsRequest getRouteDetailsRequest;
    private ImageButton ibCar;
    private ImageButton ibPublicTransport;
    private ImageButton ibBicycle;
    private ImageButton ibWalk;
    private GoogleTransportMode transportMode = GoogleTransportMode.driving;
    private String startPoint;
    private String destinationPoint;
    public static DateTime dateTime1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.accessToken));
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Set Route");
        setSupportActionBar(toolbar);

        //getSupportActionBar().hide();


//        // "https://maps.googleapis.com/maps/api/" +
//        "directions/json?origin=place_id:ChIJ8b5DJgJaIkcRqtOAYilxmD4&destination=place_id:" +
//                "ChIJncLRe9ZZIkcRtBg-8THvidU&key=AIzaSyCFa5n3POS1VSsNgn8NKORx8pGfLSTYBGU"
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        bSetRoute = (Button) findViewById(R.id.bSetRoute);
        bNext = (Button) findViewById(R.id.bNext);
        bClean = (Button) findViewById(R.id.bClean);
        mapView = (MapView) findViewById(R.id.mapview);
        //mapView.setStyleUrl(Style.MAPBOX_STREETS);
        tvRouteTime = (TextView) findViewById(R.id.tvRouteTime);
        ibCar = (ImageButton) findViewById(R.id.ibCarTransport);
        ibPublicTransport = (ImageButton) findViewById(R.id.ibPublicTransport);
        ibBicycle = (ImageButton) findViewById(R.id.ibBicycleTransport);
        ibWalk = (ImageButton) findViewById(R.id.ibFootTransport);


        //tworzenie mapy
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                // Customize map with markers, polylines, etc.
                map = mapboxMap;
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(51.248975, 22.551762)) // Sets the new camera position
                        .zoom(8) // Sets the zoom
                        .bearing(0) // Rotate the camera
                        .tilt(0) // Set the camera tilt
                        .build(); // Creates a CameraPosition from the builder

                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 2000);

                map.addPolyline(new PolylineOptions()
                        .add(responsePoints.toArray(new LatLng[responsePoints.size()]))
                        .color(Color.parseColor("#ff0000"))
                        .alpha(1.0f)
                        .width(10));

            }
        });


        final CustomAutocompleteFragment fromAutocompleteFragment = (CustomAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.from_autocomplete_fragment);
        fromAutocompleteFragment.setHintText("From...");
        fromAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                startPoint = place.getName().toString();
                Log.i("TAG", "miejsce wyjazdu: " + place.getName());
                Log.i("TAG", "wspolrzedne: " + place.getLatLng());

                MarkerViewOptions marker = new MarkerViewOptions()
                        .position(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
                marker.title(place.getName().toString());
                markerViewFrom = map.addMarker(marker);
                Log.d("punkty", "wspolrzedne z: " + place.getLatLng());
                fromLocationId = place.getId();
                Log.d("fromLocationId 1:", fromLocationId);
                setCameraPosition(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i("TAG", "An error occurred: " + status);
            }
        });


        final CustomAutocompleteFragment toAutocompleteFragment = (CustomAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.to_autocomplete_fragment);
        toAutocompleteFragment.setHintText("To...");
        toAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                destinationPoint = place.getName().toString();
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

        ibCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(ibCar);
                transportMode = GoogleTransportMode.driving;
            }
        });

        ibPublicTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(ibPublicTransport);
                transportMode = GoogleTransportMode.transit;
            }
        });

        ibBicycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(ibBicycle);
                transportMode = GoogleTransportMode.bicycling;
            }
        });

        ibWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsReaction(ibWalk);
                transportMode = GoogleTransportMode.walking;
            }
        });

        bSetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fromLocationId.isEmpty() && !toLocationId.isEmpty()) {
                    setRequest();

                    //Parser parser = new Parser();
                    //ArrayList<LatLng> points = parser.parseRoutePoints();

                } else {
                    Toast.makeText(MainActivity.this, "No location", Toast.LENGTH_LONG).show();
                }

            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Uzupelnianie AlarmPOJO 4 z 8 pol
                if (routeTimeInSeconds > 0) {

                    setAlarmPOJOData(responsePoints);

                    Intent startAlarmClockActivityIntent = new Intent(MainActivity.this, ClockFaceActivity.class);

                    //startAlarmClockActivityIntent.putExtra("travelTimeInSeconds", routeTimeInSeconds);
                    MainActivity.this.startActivity(startAlarmClockActivityIntent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Route is not set", Toast.LENGTH_LONG).show();
                }
            }
        });

        bClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                fromAutocompleteFragment.setText("");
                toAutocompleteFragment.setText("");
            }
        });

        new MyAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_alarm) {

        }

        if (id == R.id.action_show_alarms_list) {
            Intent startAlarmsListActivityIntent = new Intent(this, AlarmsListActivity.class);
            this.startActivity(startAlarmsListActivityIntent);
            this.finish();
        }

        if (id == R.id.action_setting) {

        }

        if (id == R.id.action_about) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Copyright \u00a9 2017\nKamil Poznakowski\nkampoznak@gmail.com");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            dateTime1 = new DateTime();
            return null;
        }
    }


    private void buttonsReaction(ImageButton imageButton) {
        ibCar.setBackgroundColor(Color.BLACK);
        ibPublicTransport.setBackgroundColor(Color.BLACK);
        ibBicycle.setBackgroundColor(Color.BLACK);
        ibWalk.setBackgroundColor(Color.BLACK);
        ibCar.setColorFilter(null);
        ibPublicTransport.setColorFilter(null);
        ;
        ibBicycle.setColorFilter(null);
        ;
        ibWalk.setColorFilter(null);
        //imageButton.setBackgroundColor(Color.GREEN);
        //imageButton.setBackgroundColor(0xFF00FF00);
        imageButton.setColorFilter(getResources().getColor(R.color.colorMyLightGreen));
    }

    private void setRequest() {
        getRouteDetailsRequest = new GetRouteDetailsRequest(this, fromLocationId, toLocationId, transportMode);
        Log.d("fromLocationId 2:", fromLocationId);
        getRouteDetailsRequest.setOnResponseListener(new OnResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                String stringRoutePoints = Parser.parseRoutePoints(response);
                if (!stringRoutePoints.isEmpty()) {

                    responsePoints = GoogleDirectionsHelper.decodePoly(stringRoutePoints);//= Parser.parseDirections(response);
                    routeTimeLabel = Parser.parseWholeRouteTime(response);
                    routeTimeInSeconds = Parser.parseRouteTimeInSekonds(response);

                    MapBoxHelper mapBoxHelper = new MapBoxHelper(map);

                    //                MarkerViewOptions markerFrom = new MarkerViewOptions()
                    //                        .position(responsePoints.get(0));
                    //                MarkerViewOptions markerTo = new MarkerViewOptions()
                    //                        .position(responsePoints.get(responsePoints.size()-1));

                    //4 linijki - ustawienie pinów:
                    markerViewFrom.setPosition(responsePoints.get(0));  //pozycja poczatkowa trasy
                    markerViewFrom.setAnchor(0.5f, 1.0f);
                    markerViewTo.setPosition(responsePoints.get(responsePoints.size() - 1)); //pozycja koncowa
                    markerViewTo.setAnchor(0.5f, 1.0f);
                    //mapBoxHelper.drawSimplify(responsePoints, map);
                    mapBoxHelper.drawBeforeSimplify(responsePoints);
                    tvRouteTime.setText("Travel time: " + routeTimeLabel);

                    mapBoxHelper.fitZoom(markerViewFrom.getPosition(), markerViewTo.getPosition());
                }


            }


            @Override
            public void onFailure() {
                Log.d("INTERFEJS", "ERROR");
            }
        });
        getRouteDetailsRequest.execute();
    }

    // AlarmPOJO przypisanie responsePoints na latLngRealm
    private void createPointsListForTravel(ArrayList<LatLng> responsePoints) {

        for (int i = 0; i < Cache.getAlarmPOJO().getLngLatPointsRealmList().size(); i++) {
            LatLngRealm latLngRealm = new LatLngRealm();
            double latitude = responsePoints.get(i).getLatitude();
            double longitude = responsePoints.get(i).getLongitude();
            latLngRealm.setLatitude(latitude);
            latLngRealm.setLongitude(longitude);

            //alarmPOJO.getLngLatPointsRealmList().add(i, latLngRealm);

            //AlarmPOJO.LngLatPointsRealmList.add(i, latLngRealm);

        }
    }


    private void setAlarmPOJOData(ArrayList<LatLng> responsePoints) {
        AlarmPOJO alarmPOJO = Cache.clearAlarmPOJO();

        alarmPOJO.setFirstPhaseData(
                routeTimeLabel,
                routeTimeInSeconds,
                startPoint,
                destinationPoint,
                transportMode.toString(),
                fromLocationId,
                toLocationId
        );
        Log.d("fromLocationId 3:", fromLocationId);
       // createPointsListForTravel(responsePoints);
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


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

    public void setCameraPosition(double latitude, double longitude) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)) // Sets the new camera position
                .build(); // Creates a CameraPosition from the builder

        map.moveCamera(CameraUpdateFactory
                .newCameraPosition(position));
    }
}
