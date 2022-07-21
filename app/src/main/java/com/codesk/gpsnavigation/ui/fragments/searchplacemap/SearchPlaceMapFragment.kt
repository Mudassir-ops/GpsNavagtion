package com.codesk.gpsnavigation.ui.fragments.searchplacemap

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentSearchPlaceMapBinding
import com.codesk.gpsnavigation.model.adapters.TrvellingModeItemAdapter
import com.codesk.gpsnavigation.model.datamodels.TravellingModeDataModel
import com.codesk.gpsnavigation.ui.fragments.search.SearchBottomNavFragment
import com.codesk.gpsnavigation.ui.fragments.searchplacesmap.SearchPlacesMapFragment
import com.codesk.gpsnavigation.utill.commons.AppConstants
import com.codesk.gpsnavigation.utill.commons.AppConstants.Companion.mCurrentLocation
import com.google.gson.JsonObject
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgressState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class SearchPlaceMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentSearchPlaceMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var trvellingModeItemAdapter: TrvellingModeItemAdapter
    var searcItemList = ArrayList<TravellingModeDataModel>()


    private var locationComponent: LocationComponent? = null
    private var isInTrackingMode = false
    private var mapView: MapView? = null
    private var currentRoute: DirectionsRoute? = null
    private var client: MapboxDirections? = null
    private var origin: Point? = null
    private var destination: Point? = null
    var navigation: MapboxNavigation? = null

    //--search map variables
    private val REQUEST_CODE_AUTOCOMPLETE = 1

    /*private var mapboxMap: MapboxMap? = null*/
    private var home: CarmenFeature? = null
    private var work: CarmenFeature? = null
    private val geojsonSourceLayerId = "geojsonSourceLayerId"
    private val symbolIconId = "symbolIconId"
    //--search map variables

    private var selectedLatitude=0.0
    private var selectedLongitude=0.0
    private var selectedPlaceName=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        _binding = FragmentSearchPlaceMapBinding.inflate(inflater, container, false)

        selectedLatitude=requireArguments().getDouble(SearchPlacesMapFragment.SEARCHEDLATITUDE)
        selectedLongitude=requireArguments().getDouble(SearchPlacesMapFragment.SEARCHEDLONGITUDE)
        selectedPlaceName=requireArguments().getString(SearchPlacesMapFragment.SEARCHEDNAME,"")

        binding.apply {
            placeName.text= selectedPlaceName

            mapView.getMapAsync(OnMapReadyCallback { mapboxMap ->
                SearchPlaceMapFragment.mapboxMap = mapboxMap
                mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                    origin =
                        Point.fromLngLat(mCurrentLocation!!.longitude, mCurrentLocation!!.latitude)
                    destination = Point.fromLngLat(selectedLongitude, selectedLatitude)
                    enableLocationComponent(style)
                    initSource(style)
                    initLayers(style)
                    getRoute(mapboxMap, origin, destination, DirectionsCriteria.PROFILE_DRIVING)
                }
            })

            ivNavigateOut.setOnClickListener {
                val permissionCheck =
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE)

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    requestReadPhoneStatePermission(requireActivity())
                } else {
                    val originPoint = Point.fromLngLat(AppConstants.mCurrentLocation!!.longitude, AppConstants.mCurrentLocation!!.latitude)
                    val destinationPoint = Point.fromLngLat(selectedLongitude, selectedLatitude)
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

              /*  val originPoint = Point.fromLngLat(AppConstants.mCurrentLocation!!.longitude, AppConstants.mCurrentLocation!!.latitude)
                val destinationPoint = Point.fromLngLat(selectedLongitude, selectedLatitude)
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

                             val optionsNavigate = NavigationViewOptions.builder()
                             optionsNavigate.progressChangeListener { location, routeProgress ->
                                 Log.v("RES", routeProgress.currentState().toString())
                                 if (routeProgress.currentState() == RouteProgressState.ROUTE_ARRIVED) {
                                     Toast.makeText(requireContext(), "Arrived", Toast.LENGTH_SHORT).show()
                                     Log.d("Arrived", "onResponse: Arrived")
                                 }
                             }
                        }

                        override fun onFailure(
                            call: Call<DirectionsResponse?>?,
                            throwable: Throwable?
                        ) {
                        }
                    })*/


            }


            searchTextview.setOnClickListener {
                initSearchFab()
            }
            trvellingModeItemAdapter = TrvellingModeItemAdapter(requireContext()) {

                when (it) {
                    0 -> {
                        mapView.getMapAsync(OnMapReadyCallback { mapboxMap ->
                            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                                origin = Point.fromLngLat(73.1970709610506, 33.488719745423296)
                                destination = Point.fromLngLat(selectedLongitude, selectedLatitude)

                                initSource(style)
                                initLayers(style)
                                getRoute(
                                    mapboxMap,
                                    origin,
                                    destination,
                                    DirectionsCriteria.PROFILE_DRIVING
                                )
                            }
                        })

                    }
                    1 -> {
                        mapView.getMapAsync(OnMapReadyCallback { mapboxMap ->
                            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                                origin = Point.fromLngLat(73.1970709610506, 33.488719745423296)
                                destination = Point.fromLngLat(selectedLongitude, selectedLatitude)

                                initSource(style)
                                initLayers(style)
                                getRoute(
                                    mapboxMap,
                                    origin,
                                    destination,
                                    DirectionsCriteria.PROFILE_CYCLING
                                )
                            }
                        })
                    }
                    else -> {
                        mapView.getMapAsync(OnMapReadyCallback { mapboxMap ->
                            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                                origin = Point.fromLngLat(73.1970709610506, 33.488719745423296)
                                destination = Point.fromLngLat(selectedLongitude, selectedLatitude)

                                initSource(style)
                                initLayers(style)
                                getRoute(
                                    mapboxMap,
                                    origin,
                                    destination,
                                    DirectionsCriteria.PROFILE_WALKING
                                )
                            }
                        })
                    }

                }

                Toast.makeText(requireContext(), "item clicked", Toast.LENGTH_SHORT).show()
            }

            rvTravellingMode.apply {
                adapter = trvellingModeItemAdapter
            }

            ivSaveMap.setOnClickListener {
                requireContext().showDialog(
                    title = "Save Map",
                    description = resources.getString(R.string.text_privacy_policy),
                    titleOfPositiveButton = "Cancel",
                    titleOfNegativeButton = "Save",
                    positiveButtonFunction = {

                    })
            }
        }
        trvellingModeItemAdapter.setTravellingModeitemList(getTravellingModeList())

        return binding.root
    }

    companion object {
        private const val ROUTE_LAYER_ID = "route-layer-id"
        private const val ROUTE_SOURCE_ID = "route-source-id"
        private const val ICON_LAYER_ID = "icon-layer-id"
        private const val ICON_SOURCE_ID = "icon-source-id"
        const val RED_PIN_ICON_ID = "red-pin-icon-id"
        var mapboxMap: MapboxMap? = null
        private val DBFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    }

    private fun getTravellingModeList(): ArrayList<TravellingModeDataModel> {
        searcItemList.clear()
        searcItemList.add(
            TravellingModeDataModel(
                travellingModeTitle = "5 hours",
                R.drawable.ic_baseline_directions_car_filled_24, true
            )
        )

        searcItemList.add(
            TravellingModeDataModel(
                travellingModeTitle = "8 hours",
                R.drawable.ic_baseline_directions_bike_24, false
            )
        )
        searcItemList.add(
            TravellingModeDataModel(
                travellingModeTitle = "20 hours",
                R.drawable.ic_baseline_directions_walk_24, false
            )
        )

        return searcItemList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun Context.showDialog(
        title: String,
        description: String,
        titleOfPositiveButton: String? = null,
        titleOfNegativeButton: String? = null,
        positiveButtonFunction: (() -> Unit)? = null,
        negativeButtonFunction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.save_map_dialouge_layout)

        val dialogPositiveButton = dialog.findViewById(R.id.btn_cancel) as AppCompatButton
        val dialogNegativeButton = dialog.findViewById(R.id.btn_save) as AppCompatButton

        dialogPositiveButton.setOnClickListener {
            positiveButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialogNegativeButton.setOnClickListener {
            negativeButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mapView != null) {
            binding.mapView.onDestroy()
        }

    }

    private fun initSource(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(ROUTE_SOURCE_ID))
        val iconGeoJsonSource = GeoJsonSource(
            ICON_SOURCE_ID, FeatureCollection.fromFeatures(
                arrayOf(
                    Feature.fromGeometry(
                        Point.fromLngLat(
                            origin!!.longitude(),
                            origin!!.latitude()
                        )
                    ),
                    Feature.fromGeometry(
                        Point.fromLngLat(
                            destination!!.longitude(),
                            destination!!.latitude()
                        )
                    )
                )
            )
        )
        loadedMapStyle.addSource(iconGeoJsonSource)
    }

    private fun initLayers(loadedMapStyle: Style) {
        val routeLayer = LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID)

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
            PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
            PropertyFactory.lineWidth(5f),
            PropertyFactory.lineColor(resources.getColor(R.color.black))
        )
        loadedMapStyle.addLayer(routeLayer)

        Objects.requireNonNull(
            BitmapUtils.getBitmapFromDrawable(
                resources.getDrawable(R.drawable.location_mapview_icon)
            )
        )?.let {
            loadedMapStyle.addImage(
                RED_PIN_ICON_ID, it
            )
        }

// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(
            SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                PropertyFactory.iconImage(RED_PIN_ICON_ID),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconOffset(arrayOf(0f, -9f))
            )
        )
    }

    private fun getRoute(
        mapboxMap: MapboxMap?,
        origin: Point?,
        destination: Point?,
        driveProfile: String
    ) {
        client = MapboxDirections.builder()
            .origin(origin!!)
            .destination(destination!!)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(driveProfile)
            .accessToken(getString(R.string.mapbox_access_token))
            .build()

        client!!.enqueueCall(object : Callback<DirectionsResponse?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<DirectionsResponse?>,
                response: Response<DirectionsResponse?>
            ) {

                Timber.d("Response code: " + response.code())
                if (response.body() == null) {
                    Timber.e("No routes found, make sure you set the right user and access token.")
                    return
                } else if (response.body()!!.routes().size < 1) {
                    Timber.e("No routes found")
                    return
                }
                currentRoute = response.body()!!.routes()[0]

                Log.d("CUrrentRoute", "onResponse: $currentRoute")


                binding.tvTravelTime.text = convertSecondsToHoursAndMiunuts(
                    response.body()!!.routes()[0].duration()!!.toLong()
                )
                binding.tvTotalDistance.text = convertMeterToKilometer(
                    response.body()!!.routes()[0].distance()!!.toFloat()
                ).toInt().toString() + "km"

                val totalSecs = response.body()!!.routes()[0].duration()!!.toLong()
                val hours = totalSecs / 3600
                val minutes = (totalSecs % 3600) / 60
                binding.tvTravelRemainningtime.text = getArrivalTime(hours, minutes).toString()
                mapboxMap?.getStyle { style ->
                    val source = style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                    source?.setGeoJson(
                        LineString.fromPolyline(
                            currentRoute!!.geometry()!!,
                            Constants.PRECISION_6
                        )
                    )
                }


            }

            override fun onFailure(call: Call<DirectionsResponse?>, throwable: Throwable) {
                Timber.e("Error: " + throwable.message)
                Toast.makeText(
                    requireContext(), "Error: " + throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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


        }
    }

    fun convertMeterToKilometer(meter: Float): Float {
        return (meter * 0.001).toFloat()
    }

    fun convertSecondsToHoursAndMiunuts(totalSecs: Long): String {

        val hours = totalSecs / 3600
        val minutes = (totalSecs % 3600) / 60
        val seconds = totalSecs % 60
        return String.format("%02dh %02dmin", hours, minutes);

    }


    fun getArrivalTime(additionalHour: Long, additionalMinute: Long): String {

        val additionalSeconds = 0
        val sdf = SimpleDateFormat("yyyy:MM:dd:HH:mm", Locale.getDefault())
        val currentDateandTime: String = sdf.format(Date())

        var date: Date? = null
        try {
            date = sdf.parse(currentDateandTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, additionalHour.toInt())
        calendar.add(Calendar.MINUTE, additionalMinute.toInt())
        calendar.add(Calendar.SECOND, additionalSeconds)
        System.out.println("Desired Time here " + calendar.time)

        val d1: Date = calendar.time
        return DBFormat.format(d1)
    }

    fun getFormatDate(): String? {
        val date = Date()
        return DBFormat.format(date)
    }

    private fun initSearchFab() {
        addUserLocations()
        val intent = PlaceAutocomplete.IntentBuilder().accessToken(
            (if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else getString(R.string.mapbox_access_token))!!
        )
            .placeOptions(
                PlaceOptions.builder()
                    .backgroundColor(Color.parseColor("#EEEEEE"))
                    .limit(10)
                    .addInjectedFeature(home)
                    .addInjectedFeature(work)
                    .build(PlaceOptions.MODE_CARDS)
            )
            .build(requireActivity())
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        SearchPlaceMapFragment.mapboxMap = mapboxMap
        mapboxMap.setStyle(
            Style.MAPBOX_STREETS
        ) { loadedMapStyle ->
            initSearchFab()
            addUserLocations()
            Objects.requireNonNull(
                BitmapUtils.getBitmapFromDrawable(
                    resources.getDrawable(R.drawable.location_mapview_icon)
                )
            )?.let {
                loadedMapStyle.addImage(
                    symbolIconId, it
                )
            }


            setUpSource(loadedMapStyle)
            setupLayer(loadedMapStyle)
        }
    }

    private fun addUserLocations() {
        home = CarmenFeature.builder().text("Mapbox SF Office")
            .geometry(Point.fromLngLat(-122.3964485, 37.7912561))
            .placeName("50 Beale St, San Francisco, CA")
            .id("mapbox-sf")
            .properties(JsonObject())
            .build()
        work = CarmenFeature.builder().text("Mapbox DC Office")
            .placeName("740 15th Street NW, Washington DC")
            .geometry(Point.fromLngLat(-77.0338348, 38.899750))
            .id("mapbox-dc")
            .properties(JsonObject())
            .build()
    }

    private fun setUpSource(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(geojsonSourceLayerId))
    }

    private fun setupLayer(loadedMapStyle: Style) {
        loadedMapStyle.addLayer(
            SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                PropertyFactory.iconImage(symbolIconId),
                PropertyFactory.iconOffset(arrayOf(0f, -8f))
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            val selectedCarmenFeature = PlaceAutocomplete.getPlace(data)
            if (mapboxMap != null) {
                val style = mapboxMap!!.style
                if (style != null) {
                    val source = style.getSourceAs<GeoJsonSource>(geojsonSourceLayerId)
                    source?.setGeoJson(
                        FeatureCollection.fromFeatures(
                            arrayOf<Feature>(
                                Feature.fromJson(
                                    selectedCarmenFeature.toJson()
                                )
                            )
                        )
                    )
                    mapboxMap!!.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                .target(
                                    LatLng(
                                        (selectedCarmenFeature.geometry() as Point?)!!.latitude(),
                                        (selectedCarmenFeature.geometry() as Point?)!!.longitude()
                                    )
                                )
                                .zoom(14.0)
                                .build()
                        ), 4000
                    )
                }
            }
        }
    }
 private  fun requestReadPhoneStatePermission(activity: Activity?) {
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

}