package com.codesk.gpsnavigation.ui.fragments.savedmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentSavedMapBinding
import com.codesk.gpsnavigation.model.adapters.SavedMapItemAdapter
import com.codesk.gpsnavigation.model.datamodels.SavedMapDataModel

class SavedMapFragment : Fragment() {

    private var _binding: FragmentSavedMapBinding? = null
    private val binding get() = _binding!!


    private lateinit var savedMapItemAdapter: SavedMapItemAdapter
    var searcItemList = ArrayList<SavedMapDataModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedMapBinding.inflate(inflater, container, false)

        binding.apply {
            savedMapItemAdapter = SavedMapItemAdapter(requireContext()) {lat,lng->
                overlayLayout.visibility = View.VISIBLE
                /* outerLayoutFamousPlacesDetail.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#d8d8d8"))*/
            }

            topOverlayEmptyLayout.setOnClickListener {
                overlayLayout.visibility = View.INVISIBLE
            }

            rvSavedMap.apply {
                adapter = savedMapItemAdapter
            }


        }
        savedMapItemAdapter.setFamousPlacesitemList(getNearByData())


        return binding.root
    }

    private fun getNearByData(): ArrayList<SavedMapDataModel> {
        searcItemList.clear()
        searcItemList.add(
            SavedMapDataModel(
                cityName = "Shah Faisal Masjid",
                R.drawable.dummy_map
            )
        )
        searcItemList.add(
            SavedMapDataModel(
                cityName = "Paksitan Monument",
                R.drawable.dummy_map
            )
        )
        searcItemList.add(SavedMapDataModel(cityName = "Giga Mall", R.drawable.gigamall))
        searcItemList.add(
            SavedMapDataModel(
                cityName = "Lake View Park",
                R.drawable.dummy_map
            )
        )
        searcItemList.add(
            SavedMapDataModel(
                cityName = "Margala Hills",
                R.drawable.dummy_map
            )
        )
        searcItemList.add(
            SavedMapDataModel(
                cityName = "Centurus Mall",
                R.drawable.dummy_map
            )
        )



        return searcItemList
    }

    companion object {

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}