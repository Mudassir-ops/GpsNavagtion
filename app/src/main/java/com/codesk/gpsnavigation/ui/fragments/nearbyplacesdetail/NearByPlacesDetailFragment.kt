package com.codesk.gpsnavigation.ui.fragments.nearbyplacesdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentNearByPlacesDetailBinding
import com.codesk.gpsnavigation.model.adapters.NearByPlacesDetailItemAdapter
import com.codesk.gpsnavigation.model.datamodels.NearByPlacesDetailDataModel
import com.codesk.gpsnavigation.ui.fragments.nearby.NearbyBottomNavFragment


class NearByPlacesDetailFragment : Fragment() {
    private var _binding: FragmentNearByPlacesDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var nearByPlacesDetailItemAdapter: NearByPlacesDetailItemAdapter
    var searcItemList = ArrayList<NearByPlacesDetailDataModel>()

    var selectedTypeName = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearByPlacesDetailBinding.inflate(inflater, container, false)

        selectedTypeName = arguments?.getString(NearbyBottomNavFragment.SELECTEDTYPE, "")!!

        binding.apply {
            headerLayoutsecond.labelSavedMap.text = selectedTypeName
            headerLayoutsecond.backImageview.setOnClickListener {
                findNavController().navigate(R.id.navigation_nearby)
            }


            nearByPlacesDetailItemAdapter = NearByPlacesDetailItemAdapter(requireContext()) {
                overlayLayout.visibility = View.VISIBLE
            }
            rvNearByPlaceDetail.apply {
                adapter = nearByPlacesDetailItemAdapter
            }

            topOverlayEmptyLayout.setOnClickListener {
                overlayLayout.visibility = View.INVISIBLE
            }
        }

        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(getNearByData())
        return binding.root
    }

    private fun getNearByData(): ArrayList<NearByPlacesDetailDataModel> {
        searcItemList.clear()
        searcItemList.add(
            NearByPlacesDetailDataModel(
                cityName = "Shah Faisal Masjid",
                R.drawable.locationpin
            )
        )
        searcItemList.add(
            NearByPlacesDetailDataModel(
                cityName = "Paksitan Monument",
                R.drawable.locationpin
            )
        )
        searcItemList.add(NearByPlacesDetailDataModel(cityName = "Giga Mall", R.drawable.locationpin))
        searcItemList.add(
            NearByPlacesDetailDataModel(
                cityName = "Lake View Park",
                R.drawable.locationpin
            )
        )
        searcItemList.add(
            NearByPlacesDetailDataModel(
                cityName = "Margala Hills",
                R.drawable.locationpin
            )
        )
        searcItemList.add(
            NearByPlacesDetailDataModel(
                cityName = "Centurus Mall",
                R.drawable.locationpin
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