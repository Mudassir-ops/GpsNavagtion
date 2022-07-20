//package com.codesk.gpsnavigation.ui;
//
//import android.location.Location;
//import android.os.Bundle;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.codesk.gpsnavigation.R;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.mapbox.android.core.location.LocationEngine;
//import com.mapbox.api.directions.v5.models.DirectionsResponse;
//import com.mapbox.api.directions.v5.models.DirectionsRoute;
//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.geometry.LatLng;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.style.light.Position;
//import com.mapbox.services.android.navigation.v5.milestone.MilestoneEventListener;
//import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
//import com.mapbox.services.android.navigation.v5.navigation.NavigationEventListener;
//import com.mapbox.services.android.navigation.v5.offroute.OffRouteListener;
//import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
//import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class MapNavigationActivity extends AppCompatActivity implements OnMapReadyCallback,
//        MapboxMap.OnMapClickListener, ProgressChangeListener, NavigationEventListener, MilestoneEventListener, OffRouteListener {
//
//    private static final int BEGIN_ROUTE_MILESTONE = 1001;
//
//
//    MapView mapView;
//    FloatingActionButton newLocationFab;
//    Button startRouteButton;
//
//    private MapboxMap mapboxMap;
//
//    // Navigation related variables
//    private LocationEngine locationEngine;
//    private MapboxNavigation navigation;
//    private DirectionsRoute route;
//    private Position destination;
//    private Position waypoint;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));
//        setContentView(R.layout.activity_map_navigation);
//        ButterKnife.bind(this);
//
//        mapView = (MapView) findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);
//
//        navigation = new MapboxNavigation(this, getString(R.string.access_token));
//
//        LocationEngine locationEngine = LostLocationEngine.getLocationEngine(this);
//        navigation.setLocationEngine(locationEngine);
//
//        // From Mapbox to The White House
//        Position origin = Position.fromCoordinates(-77.03613, 38.90992);
//        Position destination = Position.fromCoordinates(-77.0365, 38.8977);
//
//        navigation.getRoute(origin, destination, 90f, new Callback<DirectionsResponse>() {
//            @Override
//            public void onResponse(
//                    Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
//
//            }
//        });
//
//        navigation.addNavigationEventListener(new NavigationEventListener() {
//            @Override
//            public void onRunning(boolean running) {
//
//            }
//        });
//    }
//
//    @OnClick(R.id.startRouteButton)
//    public void onStartRouteClick() {
//        if (navigation != null && route != null) {
//
//            // Hide the start button
//            startRouteButton.setVisibility(View.INVISIBLE);
//
//            // Attach all of our navigation listeners.
//            navigation.addNavigationEventListener(this);
//            navigation.addProgressChangeListener(this);
//            navigation.addMilestoneEventListener(this);
//
//            ((MockLocationEngine) locationEngine).setRoute(route);
//            navigation.setLocationEngine(locationEngine);
//            navigation.startNavigation(route);
//            mapboxMap.setOnMapClickListener(null);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Remove all navigation listeners being used
//        navigation.removeNavigationEventListener(this);
//        navigation.removeNavigationEventListener(this);
//        navigation.removeProgressChangeListener(this);
//        navigation.removeOffRouteListener(this);
//
//        // End the navigation session
//        navigation.endNavigation();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mapView.onStart();
//        navigation.onStart();
//    }
//
//    @Override
//    public boolean onMapClick(@NonNull LatLng point) {
//
//        return false;
//    }
//
//    @Override
//    public void onMapReady(MapboxMap mapboxMap) {
//
//    }
//
//    @Override
//    public void onRunning(boolean running) {
//
//    }
//
//    @Override
//    public void onMilestoneEvent(RouteProgress routeProgress, String instruction, int identifier) {
//
//    }
//
//    @Override
//    public void onProgressChange(Location location, RouteProgress routeProgress) {
//
//    }
//
//    @Override
//    public void userOffRoute(Location location) {
//
//    }
//}