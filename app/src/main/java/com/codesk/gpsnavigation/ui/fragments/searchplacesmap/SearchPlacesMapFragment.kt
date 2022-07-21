package com.codesk.gpsnavigation.ui.fragments.searchplacesmap

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentFamousPlaceMapBinding
import com.codesk.gpsnavigation.databinding.FragmentSearchPlaceMapBinding
import com.codesk.gpsnavigation.databinding.FragmentSearchPlacesMapBinding
import com.codesk.gpsnavigation.model.adapters.ChildAdapter
import com.codesk.gpsnavigation.ui.fragments.famousplacesmap.FamousPlaceMapFragment
import com.codesk.gpsnavigation.ui.fragments.search.SearchBottomNavFragment
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils
import timber.log.Timber


class SearchPlacesMapFragment : Fragment() {

    private var _binding: FragmentSearchPlacesMapBinding? = null
    private val binding get() = _binding!!

    private var mapView: MapView? = null
    private var symbolManager: SymbolManager? = null
    private var symbol: Symbol? = null

    private var selectedLatitude=0.0
    private var selectedLongitude=0.0
    private var selectedPlaceName=""


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        _binding = FragmentSearchPlacesMapBinding.inflate(inflater, container, false)

        selectedLatitude=requireArguments().getDouble(SearchBottomNavFragment.SEARCHEDLATITUDE)
        selectedLongitude=requireArguments().getDouble(SearchBottomNavFragment.SEARCHEDLONGITUDE)
        selectedPlaceName=requireArguments().getString(SearchBottomNavFragment.SEARCHEDNAME,"")

        binding.apply {
            tvPlaceName.text=selectedPlaceName

           ivShowOnTheMap.setOnClickListener {
               val bundle = Bundle()
               bundle.putDouble(SEARCHEDLATITUDE, selectedLatitude)
               bundle.putDouble(SEARCHEDLONGITUDE, selectedLongitude)
               bundle.putString(SEARCHEDNAME,selectedPlaceName)
               findNavController().navigate(R.id.navigation_search_place_map,bundle)
           }


        }

        binding.mapView.getMapAsync(OnMapReadyCallback { mapboxMap: MapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style: Style ->
                mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2.0))
                addAirplaneImageToStyle(style)

                // create symbol manager
                val geoJsonOptions = GeoJsonOptions().withTolerance(0.4f)
                symbolManager = SymbolManager(binding.mapView, mapboxMap, style, null, geoJsonOptions)


                mapboxMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                            .target(
                                LatLng(
                                    selectedLatitude,
                                    selectedLongitude
                                )
                            )
                            .zoom(14.0)
                            .build()
                    ), 4000
                )

                // set non data driven properties
                symbolManager!!.iconAllowOverlap = false
                symbolManager!!.textAllowOverlap = false

                // create a symbol
                val symbolOptions = SymbolOptions()
                    .withLatLng(LatLng(selectedLatitude, selectedLongitude))
                    .withIconImage(ID_ICON_AIRPORT)
                    .withIconSize(0.6f)
                    .withSymbolSortKey(10.0f)
                    .withDraggable(false)
                symbol = symbolManager!!.create(symbolOptions)
                Timber.e(symbol.toString())
            }
        })

        return binding.root
    }


    private fun addAirplaneImageToStyle(style: Style) {
        style.addImage(
            ID_ICON_AIRPORT,
            BitmapUtils.getBitmapFromDrawable(resources.getDrawable(R.drawable.airplane))!!,
            true
        )

    }

    companion object {
        private const val ID_ICON_AIRPORT = "airport"
        val TAG = "FamPlcMapFragTAG"

        val SEARCHEDLATITUDE = "SEARCHEDLATITUDE"
        val SEARCHEDLONGITUDE = "SEARCHEDLONGITUDE"
        val SEARCHEDNAME = "SEARCHEDNAME"
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
        binding.  mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.  mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (symbolManager != null) {
            symbolManager!!.onDestroy()
        }
        binding. mapView.onDestroy()
        _binding = null
    }
}