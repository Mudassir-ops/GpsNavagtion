package com.codesk.gpsnavigation.ui.fragments.nearbyplacemap

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentFamousPlaceMapBinding
import com.codesk.gpsnavigation.databinding.FragmentNearByPlacesmapBinding
import com.codesk.gpsnavigation.model.adapters.ChildAdapter
import com.codesk.gpsnavigation.model.datamodels.SavedMapTable
import com.codesk.gpsnavigation.ui.fragments.famousplacesmap.FamousPlaceMapFragmentViewModel
import com.codesk.gpsnavigation.ui.fragments.nearbyplacesdetail.NearByPlacesDetailFragment
import com.codesktech.volumecontrol.utills.commons.CommonFunctions
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class NearByPlacesmapFragment : Fragment() {


    private var _binding: FragmentNearByPlacesmapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FamousPlaceMapFragmentViewModel by viewModels()

    private var symbolManager: SymbolManager? = null
    private var symbol: Symbol? = null


    private var selectedLatitude = 0.0
    private var selectedLongitude = 0.0
    private var selectedPlaceName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        _binding = FragmentNearByPlacesmapBinding.inflate(inflater, container, false)

        selectedLatitude = requireArguments().getDouble(NearByPlacesDetailFragment.SelectedLatitude)
        selectedLongitude = requireArguments().getDouble(NearByPlacesDetailFragment.SelectedLngitude)
        selectedPlaceName = requireArguments().getString(NearByPlacesDetailFragment.SelectedPlaceNAME,"")

        binding.apply {
            ivSavedMap.setOnClickListener {
                requireContext().showDialog(
                    title = "Save Map",
                    description = "$selectedPlaceName",
                    titleOfPositiveButton = "Cancel",
                    titleOfNegativeButton = "Save",
                    positiveButtonFunction = {

                        CoroutineScope(Dispatchers.IO).launch {
                            withContext(Dispatchers.Main) {
                                viewModel.insertSavedMap(
                                    savedMapTable = SavedMapTable(
                                        savedPlaceName = it,
                                        savedPlaceLatitude = selectedLatitude,
                                        savedPlaceLongitude = selectedLongitude
                                    )
                                )
                            }
                        }
                        CommonFunctions.showMessage(
                            binding.root,
                            "$it Saved in Your Saved Maps"
                        )
                    })
            }
        }


        binding.mapView.getMapAsync(OnMapReadyCallback { mapboxMap: MapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style: Style ->
                mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2.0))
                addAirplaneImageToStyle(style)

                // create symbol manager
                val geoJsonOptions = GeoJsonOptions().withTolerance(0.4f)
                symbolManager =
                    SymbolManager(binding.mapView, mapboxMap, style, null, geoJsonOptions)


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
        if (symbolManager != null) {
            symbolManager!!.onDestroy()
        }
        binding.mapView.onDestroy()
        _binding = null
    }

    fun Context.showDialog(
        title: String,
        description: String,
        titleOfPositiveButton: String? = null,
        titleOfNegativeButton: String? = null,
        positiveButtonFunction: ((placeName:String) -> Unit)? = null,
        negativeButtonFunction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.save_map_dialouge_layout)

        val dialogPositiveButton = dialog.findViewById(R.id.btn_save) as AppCompatButton
        val dialogNegativeButton = dialog.findViewById(R.id.btn_cancel) as AppCompatButton

        val dialogEdttextPlaceName = dialog.findViewById(R.id.edt_save_title) as EditText

        dialogEdttextPlaceName.setText(description)

        dialogPositiveButton.setOnClickListener {
            positiveButtonFunction?.invoke(dialogEdttextPlaceName.getText().toString())
            dialog.dismiss()
        }
        dialogNegativeButton.setOnClickListener {
            negativeButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }
}