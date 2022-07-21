package com.codesk.gpsnavigation.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.animation.ValueAnimator
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.codesk.gpsnavigation.R
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.codesk.gpsnavigation.ui.activities.SymbolActivity
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import timber.log.Timber
import com.mapbox.mapboxsdk.utils.BitmapUtils
import java.util.*

class SymbolActivity : AppCompatActivity() {
    private val random = Random()
    private val animators: List<ValueAnimator?> = ArrayList<Any?>()
    private var mapView: MapView? = null
    private var symbolManager: SymbolManager? = null
    private var symbol: Symbol? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this@SymbolActivity, getString(R.string.mapbox_access_token))
        setContentView(R.layout.actvity_notation)
        mapView = findViewById(R.id.mapView)
        if (savedInstanceState != null) {
            mapView.onCreate(savedInstanceState)
        }
        mapView.getMapAsync(OnMapReadyCallback { mapboxMap: MapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style: Style ->
                mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2.0))
                addAirplaneImageToStyle(style)

                // create symbol manager
                val geoJsonOptions = GeoJsonOptions().withTolerance(0.4f)
                symbolManager = SymbolManager(mapView, mapboxMap, style, null, geoJsonOptions)


                // set non data driven properties
                symbolManager!!.iconAllowOverlap = true
                symbolManager!!.textAllowOverlap = true

                // create a symbol
                val symbolOptions = SymbolOptions()
                    .withLatLng(LatLng(36.31929635062353, 74.65088884901236))
                    .withIconImage(ID_ICON_AIRPORT)
                    .withIconSize(1.3f)
                    .withSymbolSortKey(10.0f)
                    .withDraggable(true)
                symbol = symbolManager!!.create(symbolOptions)
                Timber.e(symbol.toString())
            }
        })
    }

    private fun addAirplaneImageToStyle(style: Style) {
        style.addImage(
            ID_ICON_AIRPORT,
            BitmapUtils.getBitmapFromDrawable(resources.getDrawable(R.drawable.airplane))!!,
            true
        )
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        for (animator in animators) {
            animator!!.cancel()
        }
        mapView!!.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (symbolManager != null) {
            symbolManager!!.onDestroy()
        }
        mapView!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    companion object {
        private const val ID_ICON_AIRPORT = "airport"
        private const val MAKI_ICON_CAR = "car-15"
        private const val MAKI_ICON_CAFE = "cafe-15"
        private const val MAKI_ICON_CIRCLE = "fire-station-15"
    }
}