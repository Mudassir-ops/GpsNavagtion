package com.codesk.gpsnavigation.ui.fragments.famousplacesmap

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentFamousPlaceMapBinding
import com.codesk.gpsnavigation.model.adapters.ChildAdapter
import com.codesk.gpsnavigation.model.datamodels.SavedMapTable
import com.codesk.gpsnavigation.utill.commons.SharedPreferencesUtil
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
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class FamousPlaceMapFragment : Fragment() {

    private var _binding: FragmentFamousPlaceMapBinding? = null
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
        _binding = FragmentFamousPlaceMapBinding.inflate(inflater, container, false)

        selectedLatitude = requireArguments().getDouble(ChildAdapter.SelectedLatitude)
        selectedLongitude = requireArguments().getDouble(ChildAdapter.SelectedLngitude)
        selectedPlaceName = requireArguments().getString(ChildAdapter.SelectedPlaceNAME, "")

        binding.apply {

            locName.text = selectedPlaceName
            backImageview.setOnClickListener {
                if (SharedPreferencesUtil(requireActivity()).isFamousPlacesSelected()) {
                    findNavController().popBackStack(R.id.navigation_famousplaces_map, true)
                    findNavController().popBackStack(R.id.navigation_famousplaces, true)
                    findNavController().popBackStack(R.id.navigation_famous_places_detail, true)
                    findNavController().navigate(R.id.navigation_famousplaces)

                } else {
                    findNavController().popBackStack(R.id.navigation_famousplaces_map, true)
                    findNavController().popBackStack(R.id.navigation_menu, true)
                    findNavController().navigate(R.id.navigation_menu)
                }

            }

            ivSavedMap.setOnClickListener {
                requireContext().showDialog(
                    title = "Save Map",
                    description = "$selectedPlaceName",
                    titleOfPositiveButton = "Cancel",
                    titleOfNegativeButton = "Save",
                    positiveButtonFunction = {

                        CoroutineScope(IO).launch {
                            withContext(Main) {
                                viewModel.insertSavedMap(
                                    savedMapTable = SavedMapTable(
                                        savedPlaceName = it,
                                        savedPlaceLatitude = selectedLatitude,
                                        savedPlaceLongitude = selectedLongitude
                                    )
                                )
                            }
                        }
                        requireContext().showDialogSavedMap()
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
        positiveButtonFunction: ((placeName: String) -> Unit)? = null,
        negativeButtonFunction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
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


    fun Context.showDialogSavedMap(
    ) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.save_map_success)

        val dialogPositiveButton = dialog.findViewById(R.id.btn_save) as AppCompatButton

        dialogPositiveButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}