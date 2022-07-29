package com.codesk.gpsnavigation.ui.fragments.homefragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentHomeBinding
import com.codesk.gpsnavigation.ui.fragments.nearby.NearbyBottomNavFragment
import com.codesk.gpsnavigation.utill.commons.AppConstants
import com.codesk.gpsnavigation.utill.commons.CommonFunctions
import com.codesk.gpsnavigation.utill.commons.CommonFunctions.Companion.showNoInternetDialog
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
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

    private var mCurrentLocation: android.location.Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest? = null
    private var locationCallback1: LocationCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        mapView = binding.mapView
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
            locationComponent!!.isLocationComponentEnabled = true
            locationComponent!!.cameraMode = CameraMode.TRACKING
            locationComponent!!.renderMode = RenderMode.GPS
            locationComponent!!.addOnLocationClickListener(this)
            locationComponent!!.addOnCameraTrackingChangedListener(this)
            binding.backToCameraTrackingMode.setOnClickListener(
                View.OnClickListener {
                    if (!isInTrackingMode) {
                        isInTrackingMode = true
                        locationComponent!!.cameraMode = CameraMode.TRACKING
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
            if (CommonFunctions.checkForInternet(requireContext())) {
                setUpLocationListener()
            } else {
                requireContext().showNoInternetDialog(
                    title = "Privacy Policy",
                    description = "",
                    titleOfPositiveButton = "",
                    titleOfNegativeButton = "",
                    positiveButtonFunction = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val panelIntent = Intent(
                                Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                            startActivityForResult(panelIntent, 402)
                        } else {
                            (this.context?.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.apply {
                                isWifiEnabled = true /*or false*/
                            }
                        }
                    }
                )
            }

        binding.mapView.onResume()
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
        binding.mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "HomeFragmentTAG"
    }

    private fun setUpLocationListener() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (mCurrentLocation == null) {
            locationRequest = LocationRequest.create().apply {
                interval = 100
                fastestInterval = 50
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                maxWaitTime = 100
                smallestDisplacement = 0f
            }
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(locationRequest!!).build()

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
            locationCallback1 = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    mCurrentLocation = locationResult.lastLocation
                    AppConstants.mCurrentLocation = mCurrentLocation
                    if (AppConstants.mCurrentLocation == null) {
                        setUpLocationListener()
                    } else {
                        stopLocationUpdates()
                    }
                    Log.d("CurrentLocation", "onLocationResult:$mCurrentLocation ")
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest!!,
                locationCallback1!!,
                Looper.myLooper()
            )
        }
    }


    private fun stopLocationUpdates() {
        Log.d(NearbyBottomNavFragment.TAG, "stopLocationUpdates: ")
        try {
            val voidTask: Task<Void> =
                fusedLocationProviderClient.removeLocationUpdates(locationCallback1!!)
            if (voidTask.isSuccessful) {
                Log.d(NearbyBottomNavFragment.TAG, "StopLocation updates successful! ")
            } else {
                Log.d(NearbyBottomNavFragment.TAG, "StopLocation updates unsuccessful! $voidTask")
            }
        } catch (exp: SecurityException) {
            Log.d(NearbyBottomNavFragment.TAG, " Security exception while removeLocationUpdates")
        }

    }
}