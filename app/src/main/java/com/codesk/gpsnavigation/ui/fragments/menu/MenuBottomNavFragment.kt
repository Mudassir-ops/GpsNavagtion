package com.codesk.gpsnavigation.ui.fragments.menu

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentMenuBottomNavigationBinding
import com.codesk.gpsnavigation.utill.commons.AppConstants
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuBottomNavFragment : Fragment() {

    private var _binding: FragmentMenuBottomNavigationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        _binding = FragmentMenuBottomNavigationBinding.inflate(inflater, container, false)

        requestReadPhoneStatePermission(requireActivity())


        //    val originPoint = Point.fromLngLat(-80.311641, 25.910195)
        val originPoint = Point.fromLngLat(
            AppConstants.mCurrentLocation!!.longitude,
            AppConstants.mCurrentLocation!!.latitude
        )

        val destinationPoint = Point.fromLngLat(73.15633151317878, 33.51551718587875)

        binding.navigation.setOnClickListener {

            NavigationRoute.builder(requireContext())
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
                            .shouldSimulateRoute(false)
                            .build()
                        NavigationLauncher.startNavigation(requireActivity(), options)

                        /* val optionsNavigate = NavigationViewOptions.builder()
                         optionsNavigate.progressChangeListener { location, routeProgress ->
                             Log.v("RES", routeProgress.currentState().toString())
                             if (routeProgress.currentState() == RouteProgressState.ROUTE_ARRIVED) {
                                 // Execute arrival logic
                             }
                         }*/
                    }

                    override fun onFailure(
                        call: Call<DirectionsResponse?>?,
                        throwable: Throwable?
                    ) {
                    }
                })
        }





        return binding.root
    }

    fun requestReadPhoneStatePermission(activity: Activity?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (activity != null) {
                    ActivityCompat.requestPermissions(
                        activity, arrayOf(
                            Manifest.permission.READ_PHONE_STATE
                        ),
                        0x1
                    )
                }
            }
        }
    }


    companion object {

    }
}