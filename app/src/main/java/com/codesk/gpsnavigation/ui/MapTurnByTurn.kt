package com.codesk.gpsnavigation.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.codesk.gpsnavigation.R
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
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

        val mapboxGeocoding = MapboxGeocoding.builder()
            .accessToken(resources.getString(R.string.mapbox_access_token))
            .query("airport")
            .query(Point.fromLngLat(73.02361495445687,  33.7503086650542))
            .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
            .build()

        mapboxGeocoding.enqueueCall(object : Callback<GeocodingResponse> {
            override fun onResponse(call: Call<GeocodingResponse>, response: Response<GeocodingResponse>) {

                val results = response.body()!!.features()

                if (results.size > 0) {

                    // Log the first results Point.
                    val firstResultPoint = results[0].center()
                    Log.d("sadasd", "onResponse: " + firstResultPoint!!.toString())

                } else {

                    // No result for your request were found.
                    Log.d("FragmentActivity.TAG", "onResponse: No result found")

                }
            }

            override fun onFailure(call: Call<GeocodingResponse>, throwable: Throwable) {
                throwable.printStackTrace()
            }
        })

    }

    fun statrtNavigation(){

    }

}