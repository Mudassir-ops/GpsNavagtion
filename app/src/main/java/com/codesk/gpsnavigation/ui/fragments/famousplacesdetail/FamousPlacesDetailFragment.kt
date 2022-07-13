package com.codesk.gpsnavigation.ui.fragments.famousplacesdetail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentFamousPlacesDetailBinding
import com.codesk.gpsnavigation.model.adapters.FamousPlacesItemAdapter
import com.codesk.gpsnavigation.model.datamodels.FamousPlacesDetailDataModel
import com.codesktech.volumecontrol.utills.commons.CommonFunctions.Companion.showDialog
import java.util.*

class FamousPlacesDetailFragment : Fragment() {
    private var _binding: FragmentFamousPlacesDetailBinding? = null
    private val binding get() = _binding!!


    private lateinit var famousPlacesItemAdapter: FamousPlacesItemAdapter
    var searcItemList = ArrayList<FamousPlacesDetailDataModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamousPlacesDetailBinding.inflate(inflater, container, false)


        binding.apply {
            famousPlacesItemAdapter = FamousPlacesItemAdapter(requireContext()) {
                overlayLayout.visibility = View.VISIBLE
                /* outerLayoutFamousPlacesDetail.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#d8d8d8"))*/
            }

            topOverlayEmptyLayout.setOnClickListener {
                overlayLayout.visibility = View.INVISIBLE
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
            labelSavedMap.setOnClickListener {
                findNavController().navigate(R.id.navigation_saved_map)
            }
            ivSavedMap.setOnClickListener {
                findNavController().navigate(R.id.navigation_saved_map)
            }


            rvFamousPlacesDetail.apply {
                adapter = famousPlacesItemAdapter
            }
            headerLayout.searchTextview.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {

                }

                override fun afterTextChanged(editable: Editable) {
                    val text: String = headerLayout.searchTextview.text.toString()
                        .lowercase(Locale.getDefault())
                    famousPlacesItemAdapter.filter(text)

                }
            })


        }
        famousPlacesItemAdapter.setFamousPlacesitemList(getNearByData())
        return binding.root
    }

    private fun getNearByData(): ArrayList<FamousPlacesDetailDataModel> {
        searcItemList.clear()
        searcItemList.add(
            FamousPlacesDetailDataModel(
                cityName = "Shah Faisal Masjid",
                R.drawable.fasilamosque
            )
        )
        searcItemList.add(
            FamousPlacesDetailDataModel(
                cityName = "Paksitan Monument",
                R.drawable.fasilamosque
            )
        )
        searcItemList.add(FamousPlacesDetailDataModel(cityName = "Giga Mall", R.drawable.gigamall))
        searcItemList.add(
            FamousPlacesDetailDataModel(
                cityName = "Lake View Park",
                R.drawable.fasilamosque
            )
        )
        searcItemList.add(
            FamousPlacesDetailDataModel(
                cityName = "Margala Hills",
                R.drawable.gigamall
            )
        )
        searcItemList.add(
            FamousPlacesDetailDataModel(
                cityName = "Centurus Mall",
                R.drawable.fasilamosque
            )
        )



        return searcItemList
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

    }
}