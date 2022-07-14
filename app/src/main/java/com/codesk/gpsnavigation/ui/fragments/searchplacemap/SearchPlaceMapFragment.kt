package com.codesk.gpsnavigation.ui.fragments.searchplacemap

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentSearchPlaceMapBinding
import com.codesk.gpsnavigation.model.adapters.TrvellingModeItemAdapter
import com.codesk.gpsnavigation.model.datamodels.TravellingModeDataModel
import com.codesktech.volumecontrol.utills.commons.CommonFunctions.Companion.showDialog

class SearchPlaceMapFragment : Fragment() {

    private var _binding: FragmentSearchPlaceMapBinding? = null
    private val binding get() = _binding!!


    private lateinit var trvellingModeItemAdapter: TrvellingModeItemAdapter
    var searcItemList = ArrayList<TravellingModeDataModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPlaceMapBinding.inflate(inflater, container, false)

        binding.apply {
            trvellingModeItemAdapter = TrvellingModeItemAdapter(requireContext()) {
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
                travellingModeTitle = "6 hours",
                R.drawable.ic_baseline_directions_bus_filled_24, false
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
}