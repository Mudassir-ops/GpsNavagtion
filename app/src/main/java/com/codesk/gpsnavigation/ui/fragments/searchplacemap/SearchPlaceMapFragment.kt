package com.codesk.gpsnavigation.ui.fragments.searchplacemap

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentSearchPlaceMapBinding
import com.codesk.gpsnavigation.model.adapters.TrvellingModeItemAdapter
import com.codesk.gpsnavigation.model.datamodels.TravellingModeDataModel
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.Mapbox.getApplicationContext
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*

class SearchPlaceMapFragment : Fragment() {

    private var _binding: FragmentSearchPlaceMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var trvellingModeItemAdapter: TrvellingModeItemAdapter
    var searcItemList = ArrayList<TravellingModeDataModel>()

    private var mapView: MapView? = null
    private var currentRoute: DirectionsRoute? = null
    private var client: MapboxDirections? = null
    private var origin: Point? = null
    private var destination: Point? = null
    var navigation: MapboxNavigation? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))

        _binding = FragmentSearchPlaceMapBinding.inflate(inflater, container, false)

        binding.apply {
            mapView.getMapAsync(OnMapReadyCallback { mapboxMap ->
                mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                    origin = Point.fromLngLat(73.1970709610506, 33.488719745423296)
                    destination = Point.fromLngLat(73.0397797943801, 33.71144607301427)

                    initSource(style)
                    initLayers(style)
                    //  getRoute1()
                    getRoute(mapboxMap, origin, destination, DirectionsCriteria.PROFILE_DRIVING)
                }
            })


            trvellingModeItemAdapter = TrvellingModeItemAdapter(requireContext()) {

                when (it) {
                    0 -> {
                        mapView.getMapAsync(OnMapReadyCallback { mapboxMap ->
                            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                                origin = Point.fromLngLat(73.1970709610506, 33.488719745423296)
                                destination = Point.fromLngLat(73.0397797943801, 33.71144607301427)

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
                                destination = Point.fromLngLat(73.0397797943801, 33.71144607301427)

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
                                destination = Point.fromLngLat(73.0397797943801, 33.71144607301427)

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
        private const val RED_PIN_ICON_ID = "red-pin-icon-id"
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
        binding.mapView.onDestroy()
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

                binding.tvTravelTime.text = response.body()!!.routes()[0].duration().toString()
                binding.tvTotalDistance.text = response.body()!!.routes()[0].distance().toString()

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

    fun getRoute1() {
        NavigationRoute.builder(requireContext())
            .accessToken(getString(R.string.mapbox_access_token))
            .origin(origin!!)
            .destination(destination!!)
            .build()
            .getRoute(object : Callback<DirectionsResponse?> {
                override fun onResponse(
                    call: Call<DirectionsResponse?>,
                    response: Response<DirectionsResponse?>
                ) {
                    if (response.body() == null || response.body()!!.routes().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "route is empty", Toast.LENGTH_LONG)
                            .show()
                        return
                    } else if (response.body()!!.routes().size < 1) {
                        Toast.makeText(getApplicationContext(), "size <1", Toast.LENGTH_LONG).show()
                    }
                    val route = response.body()!!.routes()[0]
                    val options = NavigationLauncherOptions.builder()
                        .directionsRoute(route)
                        .shouldSimulateRoute(true)
                        .build()
                    NavigationLauncher.startNavigation(requireActivity(), options)
                }

                override fun onFailure(call: Call<DirectionsResponse?>, t: Throwable) {}
            })
    }

}