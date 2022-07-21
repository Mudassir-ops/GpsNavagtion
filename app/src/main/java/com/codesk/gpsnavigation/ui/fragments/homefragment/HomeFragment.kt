package com.codesk.gpsnavigation.ui.fragments.homefragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentHomeBinding
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.*
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style


class HomeFragment : Fragment(), OnMapReadyCallback, OnLocationClickListener,
    OnCameraTrackingChangedListener {



    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var locationComponent: LocationComponent? = null
    private var isInTrackingMode = false


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        mapView=binding.mapView
        if (savedInstanceState != null) {
            binding.mapView.onCreate(savedInstanceState)
        }

        binding.mapView.getMapAsync(this)


        return binding.root
    }



    override fun onCameraTrackingDismissed() {
        isInTrackingMode = false
    }

    override fun onCameraTrackingChanged(currentMode: Int) {}

    override fun onLocationComponentClick() {
        if (locationComponent!!.lastKnownLocation != null) {
            Toast.makeText(
                requireContext(), String.format(
                    getString(R.string.current_location),
                    locationComponent!!.lastKnownLocation!!.latitude,
                    locationComponent!!.lastKnownLocation!!.longitude
                ), Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(
            Style.LIGHT
        ) { style -> enableLocationComponent(style) }
    }


    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            val customLocationComponentOptions = LocationComponentOptions.builder(requireContext())
                .elevation(5f)
                .accuracyAlpha(.6f)
                .build()
            locationComponent = mapboxMap!!.locationComponent
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()
            locationComponent!!.activateLocationComponent(locationComponentActivationOptions)
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationComponent!!.setLocationComponentEnabled(true)
            locationComponent!!.setCameraMode(CameraMode.TRACKING)
            locationComponent!!.setRenderMode(RenderMode.GPS)
            locationComponent!!.addOnLocationClickListener(this)
            locationComponent!!.addOnCameraTrackingChangedListener(this)
            binding.backToCameraTrackingMode.setOnClickListener(
                View.OnClickListener {
                    if (!isInTrackingMode) {
                        isInTrackingMode = true
                        locationComponent!!.setCameraMode(CameraMode.TRACKING)
                        locationComponent!!.zoomWhileTracking(16.0)
                        Toast.makeText(
                            requireContext(), "tracking enabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(), "already enabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding. mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.   mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.  mapView.onStop()
    }



    override fun onDestroy() {
        super.onDestroy()
        binding. mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.   mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}