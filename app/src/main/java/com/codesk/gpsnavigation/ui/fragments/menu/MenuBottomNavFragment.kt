package com.codesk.gpsnavigation.ui.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentMenuBottomNavigationBinding
import com.codesk.gpsnavigation.model.adapters.ChildAdapter
import com.codesk.gpsnavigation.model.adapters.SavedMapItemAdapter
import com.codesk.gpsnavigation.model.datamodels.SavedMapDataModel
import com.codesk.gpsnavigation.model.datamodels.SavedMapTable
import com.codesktech.volumecontrol.utills.commons.CommonFunctions.Companion.showDialog


class MenuBottomNavFragment : Fragment() {
    private var _binding: FragmentMenuBottomNavigationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MenuBottomNavViewModel by viewModels()


    private lateinit var savedMapItemAdapter: SavedMapItemAdapter
    var searcItemList = ArrayList<SavedMapDataModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBottomNavigationBinding.inflate(inflater, container, false)

        binding.apply {
            savedMapItemAdapter = SavedMapItemAdapter(requireContext()) {
                overlayLayout.visibility = View.VISIBLE
                /* outerLayoutFamousPlacesDetail.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#d8d8d8"))*/
            }

            //---actually show on map btn xml naming wrong
            ivSavedMap.setOnClickListener {
                val bundle = Bundle()
                bundle.putDouble(ChildAdapter.SelectedLatitude, 43.08287886692618)
                bundle.putDouble(ChildAdapter.SelectedLngitude, -79.07416290191135)
                findNavController().navigate(R.id.navigation_famousplaces_map, bundle)
            }


            topOverlayEmptyLayout.setOnClickListener {
                overlayLayout.visibility = View.INVISIBLE
            }

            rvSavedMap.apply {
                adapter = savedMapItemAdapter
            }

            ivPrivacyPolicy.setOnClickListener {
                requireContext().showDialog(
                    title = "Privacy Policy",
                    description = resources.getString(R.string.text_privacy_policy),
                    titleOfPositiveButton = "yes",
                    titleOfNegativeButton = "No",
                    positiveButtonFunction = {

                    })
            }
            labelPrivacyPolicy.setOnClickListener {
                requireContext().showDialog(
                    title = "Privacy Policy",
                    description = resources.getString(R.string.text_privacy_policy),
                    titleOfPositiveButton = "yes",
                    titleOfNegativeButton = "No",
                    positiveButtonFunction = {

                    })
            }

        }


        viewModel.getAllSavedMap().observe(viewLifecycleOwner, Observer {
            searcItemList.clear()
            it.map { searcItemList.add(SavedMapDataModel(cityName = "${it.savedPlaceName}", imageResource = R.drawable.dummy_map)) }
            savedMapItemAdapter.setFamousPlacesitemList(searcItemList )

        })

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