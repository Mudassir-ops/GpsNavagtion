package com.codesk.gpsnavigation


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import java.util.*


class MapCurentLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private val REQUEST_CODE_AUTOCOMPLETE = 1
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var home: CarmenFeature? = null
    private var work: CarmenFeature? = null
    private val geojsonSourceLayerId = "geojsonSourceLayerId"
    private val symbolIconId = "symbolIconId"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this@MapCurentLocationActivity, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map_curent_location)
        mapView = findViewById(R.id.mapView)
        mapView!!.getMapAsync(this@MapCurentLocationActivity)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
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

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
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

    private fun initSearchFab() {

        findViewById<View>(R.id.fab_location_search).setOnClickListener {
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
                .build(this@MapCurentLocationActivity)
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
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
                iconImage(symbolIconId),
                iconOffset(arrayOf(0f, -8f))
            )
        )
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }


}