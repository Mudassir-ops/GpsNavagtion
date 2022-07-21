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
import com.codesk.gpsnavigation.model.adapters.ChildAdapter
import com.codesk.gpsnavigation.model.adapters.FamousPlacesItemAdapter
import com.codesk.gpsnavigation.model.adapters.ParentAdapter
import com.codesk.gpsnavigation.model.datamodels.FamousPlacesDetailDataModel
import com.codesktech.volumecontrol.utills.commons.CommonFunctions.Companion.showDialog
import java.util.*

class FamousPlacesDetailFragment : Fragment() {
    private var _binding: FragmentFamousPlacesDetailBinding? = null
    private val binding get() = _binding!!


    private lateinit var famousPlacesItemAdapter: FamousPlacesItemAdapter
    var searcItemList = ArrayList<FamousPlacesDetailDataModel>()

    var selectedCountry = -1


    private val latitudeListPaksitanFamousPlaces =
        arrayListOf(36.31957297517686, 34.59907892272706, 36.45860546466207, 36.173469457866155)
    private val lngitudeListPaksitanFamousPlaces =
        arrayListOf(74.65071718764615, 73.90733948279369, 74.8802507389999, 72.94677263496762)

    private val latitudeListAustraliaFamousPlaces = arrayListOf(
        -33.37225467233445,
        -17.95457190783468,
        -16.168850908071242,
        -33.863433909530876
    )
    private val lngitudeListAustraliaFamousPlaces =
        arrayListOf(150.28814571240719, 122.23756801822948, 145.41577950307257, 151.21022028435164)

    private val latitudeListCanadaFamousPlaces =
        arrayListOf(43.08287886692618, 46.82387386478944, 49.13469022098501, 50.11909844955745)
    private val lngitudeListCanadaFamousPlaces = arrayListOf(
        -79.07416290191135,
        -71.21329652859735,
        -125.89266792511567,
        -122.95345145414817
    )

    private val latitudeListEnglandFamousPlaces =
        arrayListOf(55.94695623192066, 57.467068267840666, 51.381212600997785, 51.48859574594411)
    private val lngitudeListEnglandFamousPlaces = arrayListOf(
        -3.215983133839371,
        -4.225806295608513,
        -2.3596941035007695,
        -0.6224845016494911
    )

    private val latitudeListSwitzerlandFamousPlaces =
        arrayListOf(46.670119964531665, 42.592253087752574, 47.05358004619934, 45.97696062635796)
    private val lngitudeListSwitzerlandFamousPlaces =
        arrayListOf(7.874476477225262, -88.4288311184804, 8.271146636584225, 7.65785105767387)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamousPlacesDetailBinding.inflate(inflater, container, false)

        selectedCountry = requireArguments().getInt(ParentAdapter.SELECTED_POSITION)

        binding.apply {
            famousPlacesItemAdapter =
                FamousPlacesItemAdapter(requireContext()) { position, latitude, lonigitude ->
                    val bundle = Bundle()
                    bundle.putDouble(ChildAdapter.SelectedLatitude, latitude)
                    bundle.putDouble(ChildAdapter.SelectedLngitude, lonigitude)
                    bundle.putString(ChildAdapter.SelectedPlaceNAME, position)
                    findNavController().navigate(R.id.navigation_famousplaces_map, bundle)

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

            when (selectedCountry) {
                0 -> {
                    searcItemList.clear()
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Hunza valley",
                            R.drawable.hunzavalley,
                            latitudeListPaksitanFamousPlaces[0],
                            lngitudeListPaksitanFamousPlaces[0]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Neelam valley",
                            R.drawable.neelamvallery,
                            latitudeListPaksitanFamousPlaces[1],
                            lngitudeListPaksitanFamousPlaces[1]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Passu Cone",
                            R.drawable.passucone,
                            latitudeListPaksitanFamousPlaces[2],
                            lngitudeListPaksitanFamousPlaces[2]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Phanchader Lake",
                            R.drawable.phancder_lake,
                            latitudeListPaksitanFamousPlaces[3],
                            lngitudeListPaksitanFamousPlaces[3]
                        )
                    )
                    famousPlacesItemAdapter.setFamousPlacesitemList(searcItemList)
                }
                1 -> {
                    searcItemList.clear()
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Blue Mountains",
                            R.drawable.blue_mountains_national_park,
                            latitudeListAustraliaFamousPlaces[0],
                            lngitudeListAustraliaFamousPlaces[0]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Broom Western",
                            R.drawable.broome_western,
                            latitudeListAustraliaFamousPlaces[1],
                            lngitudeListAustraliaFamousPlaces[1]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Dain Tree National",
                            R.drawable.daintree_nationalpark,
                            latitudeListAustraliaFamousPlaces[2],
                            lngitudeListAustraliaFamousPlaces[2]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Sydney Bridge",
                            R.drawable.sydneyharbourbridge,
                            latitudeListAustraliaFamousPlaces[3],
                            lngitudeListAustraliaFamousPlaces[3]
                        )
                    )
                    famousPlacesItemAdapter.setFamousPlacesitemList(searcItemList)
                }
                2 -> {
                    searcItemList.clear()
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Niagra Fall",
                            R.drawable.niagrafall,
                            latitudeListCanadaFamousPlaces[0],
                            lngitudeListCanadaFamousPlaces[0]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Quebec City",
                            R.drawable.quebeccity,
                            latitudeListCanadaFamousPlaces[1],
                            lngitudeListCanadaFamousPlaces[1]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Tofino City",
                            R.drawable.tofino,
                            latitudeListCanadaFamousPlaces[2],
                            lngitudeListCanadaFamousPlaces[2]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Whistler",
                            R.drawable.whistler,
                            latitudeListCanadaFamousPlaces[3],
                            lngitudeListCanadaFamousPlaces[3]
                        )
                    )
                    famousPlacesItemAdapter.setFamousPlacesitemList(searcItemList)
                }
                3 -> {
                    searcItemList.clear()
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Edinburgh",
                            R.drawable.edinburgh,
                            latitudeListEnglandFamousPlaces[0],
                            lngitudeListEnglandFamousPlaces[0]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Inverness",
                            R.drawable.lochnessandinverness,
                            latitudeListEnglandFamousPlaces[1],
                            lngitudeListEnglandFamousPlaces[1]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Roman rra bath",
                            R.drawable.romanerabath,
                            latitudeListEnglandFamousPlaces[2],
                            lngitudeListEnglandFamousPlaces[2]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Royal Windsor",
                            R.drawable.royalwindsor,
                            latitudeListEnglandFamousPlaces[3],
                            lngitudeListEnglandFamousPlaces[3]
                        )
                    )
                    famousPlacesItemAdapter.setFamousPlacesitemList(searcItemList)
                }
                else -> {
                    searcItemList.clear()
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Interlaken",
                            R.drawable.interlaken,
                            latitudeListSwitzerlandFamousPlaces[0],
                            lngitudeListSwitzerlandFamousPlaces[0]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Lake Geneva",
                            R.drawable.lakegeneva,
                            latitudeListSwitzerlandFamousPlaces[1],
                            lngitudeListSwitzerlandFamousPlaces[1]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Lucerne",
                            R.drawable.lucerne,
                            latitudeListSwitzerlandFamousPlaces[2],
                            lngitudeListSwitzerlandFamousPlaces[2]
                        )
                    )
                    searcItemList.add(
                        FamousPlacesDetailDataModel(
                            cityName = "Matterhorn",
                            R.drawable.matterhorn,
                            latitudeListSwitzerlandFamousPlaces[3],
                            lngitudeListSwitzerlandFamousPlaces[3]
                        )
                    )
                    famousPlacesItemAdapter.setFamousPlacesitemList(searcItemList)
                }

            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}