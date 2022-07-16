package com.codesk.gpsnavigation;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    MapboxNavigation navigation;

    Point origin = Point.fromLngLat(73.1970709610506, 33.488719745423296);
    Point destination = Point.fromLngLat(73.0397797943801, 33.71144607301427);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));
        navigation = new MapboxNavigation(getApplicationContext(), getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_test);


        getRoute();

    }

    protected void getRoute() {

        NavigationRoute.builder(this)
                .accessToken(getString(R.string.mapbox_access_token))
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null || response.body().routes().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "route is empty", Toast.LENGTH_LONG).show();
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Toast.makeText(getApplicationContext(), "size <1", Toast.LENGTH_LONG).show();
                        }

                        DirectionsRoute route = response.body().routes().get(0);

                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(route)
                                .shouldSimulateRoute(true)
                                .build();
                        NavigationLauncher.startNavigation(TestActivity.this, options);


                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigation.onDestroy();
    }
}