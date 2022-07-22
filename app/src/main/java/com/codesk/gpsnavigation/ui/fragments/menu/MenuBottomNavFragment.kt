package com.codesk.gpsnavigation.ui.fragments.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.BuildConfig
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentMenuBottomNavigationBinding
import com.codesk.gpsnavigation.model.adapters.ChildAdapter
import com.codesk.gpsnavigation.model.adapters.SavedMapItemAdapter
import com.codesk.gpsnavigation.model.datamodels.SavedMapDataModel
import com.codesktech.volumecontrol.utills.commons.CommonFunctions.Companion.showDialog


class MenuBottomNavFragment : Fragment() {
    private var _binding: FragmentMenuBottomNavigationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MenuBottomNavViewModel by viewModels()


    private lateinit var savedMapItemAdapter: SavedMapItemAdapter
    var searcItemList = ArrayList<SavedMapDataModel>()

    var dbLatitude = 0.0
    var dbLongitude = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBottomNavigationBinding.inflate(inflater, container, false)

        binding.apply {
            savedMapItemAdapter = SavedMapItemAdapter(requireContext()) { latitude, lngitude ->
                dbLatitude = latitude
                dbLongitude = lngitude

                overlayLayout.visibility = View.VISIBLE
                /* outerLayoutFamousPlacesDetail.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#d8d8d8"))*/
            }

            //---actually show on map btn xml naming wrong
            ivSavedMap.setOnClickListener {
                val bundle = Bundle()
                bundle.putDouble(ChildAdapter.SelectedLatitude, dbLatitude)
                bundle.putDouble(ChildAdapter.SelectedLngitude, dbLongitude)
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


            ivShareApp.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                //intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Studio Pro");
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    """Hi dear I am using Charging Animation app, it has amazing effects and animations, Install and enjoy with Charging Animation 
 https://play.google.com/store/apps/details?id=${requireContext().packageName}"""
                )
                intent.type = "text/plain"
                startActivity(intent)
            }
            labelShareApp.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                //intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Studio Pro");
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    """Hi dear I am using Charging Animation app, it has amazing effects and animations, Install and enjoy with Charging Animation 
 https://play.google.com/store/apps/details?id=${requireContext().packageName}"""
                )
                intent.type = "text/plain"
                startActivity(intent)
            }

            ivRateUs.setOnClickListener {
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + requireContext().packageName)
                    )
                    startActivity(intent)
                } catch (e: Exception) {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data =
                        Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                    startActivity(i)
                }
            }
            labelRateUs.setOnClickListener {
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + requireContext().packageName)
                    )
                    startActivity(intent)
                } catch (e: Exception) {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data =
                        Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                    startActivity(i)
                }
            }

            ivShareMyLocation.setOnClickListener {
                val uri = "https://www.google.com/maps/?q=$dbLatitude,$dbLongitude"
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
                startActivity(Intent.createChooser(sharingIntent, "Share in..."))
            }
            labelShareMyLocation.setOnClickListener {
                val uri = ("geo:" + dbLatitude + ","
                        + dbLongitude + "?q=" + dbLatitude + "," + dbLongitude)
                startActivity(
                    Intent(
                        Intent.ACTION_SEND,
                        Uri.parse(uri)
                    )
                )
            }
        }


        viewModel.getAllSavedMap().observe(viewLifecycleOwner, Observer { list ->
            searcItemList.clear()
            list.map {
                searcItemList.add(
                    SavedMapDataModel(
                        cityName = "${it.savedPlaceName}",
                        imageResource = R.drawable.dummy_map,
                        it.savedPlaceLatitude,
                        it.savedPlaceLongitude
                    )
                )
            }
            savedMapItemAdapter.setFamousPlacesitemList(searcItemList)

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