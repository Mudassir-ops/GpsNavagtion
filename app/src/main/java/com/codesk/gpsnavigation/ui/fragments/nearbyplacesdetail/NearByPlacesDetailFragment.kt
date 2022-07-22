package com.codesk.gpsnavigation.ui.fragments.nearbyplacesdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentNearByPlacesDetailBinding
import com.codesk.gpsnavigation.model.adapters.NearByPlacesDetailItemAdapter
import com.codesk.gpsnavigation.model.datamodels.NearByPlacesDetailDataModel
import com.codesk.gpsnavigation.ui.fragments.nearby.NearbyBottomNavFragment
import com.codesk.gpsnavigation.utill.commons.AppConstants
import com.mapbox.geojson.Point
import com.mapbox.search.*
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion


class NearByPlacesDetailFragment : Fragment() {

    private val viewModel: NearbyPlacesDetailViewModel by viewModels()

    private var _binding: FragmentNearByPlacesDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var nearByPlacesDetailItemAdapter: NearByPlacesDetailItemAdapter
    var searcItemList = ArrayList<NearByPlacesDetailDataModel>()

    var selectedTypeName = ""
    var SELECTEDPPOSITION = -1


    var latitude = 0.0
    var longitude = 0.0
    var palce = ""

    //---search suggesion list near by
    private lateinit var searchEngine: SearchEngine
    private lateinit var searchRequestTask: SearchRequestTask
    private val searchCallback = object : SearchSelectionCallback {

        override fun onSuggestions(
            suggestions: List<SearchSuggestion>,
            responseInfo: ResponseInfo
        ) {
            viewModel.showProgressBar.value = false
            if (suggestions.isEmpty()) {
                Log.i("SearchApiExample", "No suggestions found")
            } else {
                Log.d("sadasdasd", "onSuggestions:$suggestions ")
                searcItemList.clear()
                when (SELECTEDPPOSITION) {
                    0 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.airplane, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )

                                Log.d("asdsadsad", "onSuggestions: $it")
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.airplane, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }

                        }

                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    1 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.petrol_stattion, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.petrol_stattion, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }

                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    2 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.hospital, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.hospital, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude(),

                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    3 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.bank, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.bank, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    4 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.realestate, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.realestate, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    5 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.furniture, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.furniture, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    6 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.computer, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.computer, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    7 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.electronics, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.electronics, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    8 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.resturant, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.resturant, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    9 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.pet, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.pet, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    10 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.forest, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.forest, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    11 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.cafeteria, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.cafeteria, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    12 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.court, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.court, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    13 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.bakery, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.bakery, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    14 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.shop, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.shop, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    15 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.library, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.library, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }
                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    16 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.glass_shop, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.glass_shop, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    17 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.gym, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {

                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.gym, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    18 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = it.name,
                                        R.drawable.salon_shop, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = it.name,
                                        R.drawable.salon_shop, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    19 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.carpenter, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.carpenter, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }

                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    20 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.post_office, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.post_office, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    21 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.hardware, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.hardware, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    22 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.pharmacy, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.pharmacy, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    23 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.university, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.university, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    24 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.ambulance, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.ambulance, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    25 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.water, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.water, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    26 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.university, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.university, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    27 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.university, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.university, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    28 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.painter, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.painter, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    29 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.cineam, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.cineam, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    30 -> {
                        suggestions.map {
                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.jewelery, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.jewelery, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                    31 -> {
                        suggestions.map {

                            if (it.address!!.street.isNullOrEmpty()) {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.taxi, it.address!!.country!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            } else {
                                searcItemList.add(
                                    NearByPlacesDetailDataModel(
                                        cityName = "${it.name}",
                                        R.drawable.taxi, it.address!!.street!!,
                                        it.requestOptions.options.proximity!!.latitude(),
                                        it.requestOptions.options.proximity!!.longitude()
                                    )
                                )
                            }


                        }
                        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
                    }
                }
                searchRequestTask = searchEngine.select(suggestions.first(), this)
            }
        }

        override fun onResult(
            suggestion: SearchSuggestion,
            result: SearchResult,
            responseInfo: ResponseInfo
        ) {
            Log.i("SearchApiExample", "Search result: $result")
        }

        override fun onCategoryResult(
            suggestion: SearchSuggestion,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {

            Log.i("SearchpleCatagorey", "Category search results: $results")
        }

        override fun onError(e: Exception) {
            Log.i("SearchApiExample", "Search error", e)
        }
    }

    //---search suggesion list near by


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchEngine = MapboxSearchSdk.createSearchEngineWithBuiltInDataProviders(
            SearchEngineSettings(resources.getString(R.string.mapbox_access_token))
        )
        _binding = FragmentNearByPlacesDetailBinding.inflate(inflater, container, false)
        selectedTypeName = arguments?.getString(NearbyBottomNavFragment.SELECTEDTYPE, "")!!
        SELECTEDPPOSITION = arguments?.getInt(NearbyBottomNavFragment.SELECTEDPPOSITION, -1)!!
        Log.d("SelcetedTypeQuery", "onCreateView: $selectedTypeName")
        viewModel.showProgressBar.value = true

        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.circularProgressIndicatr.visibility=View.VISIBLE
            } else {
                binding.circularProgressIndicatr.visibility=View.GONE
            }

        })

        searchRequestTask = searchEngine.search("$selectedTypeName", SearchOptions(limit = 5, proximity = Point.fromLngLat(AppConstants.mCurrentLocation!!.longitude, AppConstants.mCurrentLocation!!.latitude)),
            searchCallback
        )

        binding.apply {
            headerLayoutsecond.labelSavedMap.text = selectedTypeName
            headerLayoutsecond.backImageview.setOnClickListener {
                findNavController().navigate(R.id.navigation_nearby)

            }

            binding.showOnTheMapLayoutRight.setOnClickListener {
                val bundle = Bundle()
                bundle.putDouble(SelectedLatitude, latitude)
                bundle.putDouble(SelectedLngitude, longitude)
                bundle.putString(SelectedPlaceNAME, palce)
                findNavController().navigate(R.id.navigation_famousplaces_map, bundle)
            }

            nearByPlacesDetailItemAdapter =
                NearByPlacesDetailItemAdapter(requireContext()) { pos, lat, lng, placeName ->
                    latitude = lat
                    longitude = lng
                    palce = placeName
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
        return searcItemList
    }


    override fun onDestroyView() {
        super.onDestroyView()
        searchRequestTask.cancel()
        _binding = null
    }

    companion object {
        val SelectedLatitude = "SelectedLatitude"
        val SelectedLngitude = "SelectedLngitude"
        val SelectedPlaceNAME = "SelectedPlaceNAME"
    }

}