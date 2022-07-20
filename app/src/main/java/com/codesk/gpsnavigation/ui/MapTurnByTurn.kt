package com.codesk.gpsnavigation.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.codesk.gpsnavigation.R
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapTurnByTurn : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this@MapTurnByTurn, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map_turn_by_turn)


        val originPoint = Point.fromLngLat(-80.311641, 25.910195)
        val destinationPoint = Point.fromLngLat(-80.312159, 25.911922)


        findViewById<Button>(R.id.navigation).setOnClickListener {




            NavigationRoute.builder(this@MapTurnByTurn)
                .accessToken(Mapbox.getAccessToken()!!)
                .origin(originPoint)
                .destination(destinationPoint)
                .build()
                .getRoute(object : Callback<DirectionsResponse?> {
                    override fun onResponse(
                        call: Call<DirectionsResponse?>?,
                        response: Response<DirectionsResponse?>
                    ) {
                        val currentRoute = response.body()!!.routes()[0]
                        val options = NavigationLauncherOptions.builder()
                            .directionsRoute(currentRoute)
                            .shouldSimulateRoute(true)
                            .build()
                        NavigationLauncher.startNavigation(this@MapTurnByTurn, options)

                       /* val optionsNavigate = NavigationViewOptions.builder()
                        optionsNavigate.progressChangeListener { location, routeProgress ->
                            Log.v("RES", routeProgress.currentState().toString())
                            if (routeProgress.currentState() == RouteProgressState.ROUTE_ARRIVED) {
                                // Execute arrival logic
                            }
                        }*/
                    }

                    override fun onFailure(call: Call<DirectionsResponse?>?, throwable: Throwable?) {}
                })
        }



    }

    fun statrtNavigation(){

    }

}