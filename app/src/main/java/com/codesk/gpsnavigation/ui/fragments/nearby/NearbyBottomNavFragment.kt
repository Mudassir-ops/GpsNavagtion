package com.codesk.gpsnavigation.ui.fragments.nearby

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentNearbyBottomNavigationBinding
import com.codesk.gpsnavigation.model.adapters.NearByItemAdapter
import com.codesk.gpsnavigation.model.datamodels.NearByItemDataModel
import java.util.*

class NearbyBottomNavFragment : Fragment() {

    private var _binding: FragmentNearbyBottomNavigationBinding? = null
    private val binding get() = _binding!!


    private lateinit var nearByItemAdapter: NearByItemAdapter
    var searcItemList = ArrayList<NearByItemDataModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val nearbyBottomNavViewModel =
            ViewModelProvider(this).get(NearbyBottomNavViewModel::class.java)

        _binding = FragmentNearbyBottomNavigationBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.apply {
            nearByItemAdapter =
                NearByItemAdapter(requireContext()) { _position: Int, _placeName: String ->
                    Log.d(TAG, "onCreateView: $_position")
                    val bundle = Bundle()
                    bundle.putString(SELECTEDTYPE, _placeName)
                    findNavController().navigate(R.id.navigation_nearby_place_detail, bundle)

                }
            rvNearByItem.apply {
                adapter = nearByItemAdapter
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
                    nearByItemAdapter.filter(text)

                }
            })


        }
        nearByItemAdapter.setNearByitemList(getNearByData())



        return root
    }

    private fun getNearByData(): ArrayList<NearByItemDataModel> {
        searcItemList.clear()
        searcItemList.add(NearByItemDataModel(cityName = "Airport", R.drawable.airplane))
        searcItemList.add(NearByItemDataModel(cityName = "Petrol Stattion", R.drawable.petrol_stattion))
        searcItemList.add(NearByItemDataModel(cityName = "Hospital", R.drawable.hospital))
        searcItemList.add(NearByItemDataModel(cityName = "Bank", R.drawable.bank))
        searcItemList.add(NearByItemDataModel(cityName = "Real Estate", R.drawable.realestate))
        searcItemList.add(NearByItemDataModel(cityName = "Furniture", R.drawable.furniture))
        searcItemList.add(NearByItemDataModel(cityName = "Computer", R.drawable.computer))
        searcItemList.add(NearByItemDataModel(cityName = "Electronics", R.drawable.electronics))
        searcItemList.add(NearByItemDataModel(cityName = "Resturant", R.drawable.resturant))
        searcItemList.add(NearByItemDataModel(cityName = "Pet", R.drawable.pet))
        searcItemList.add(NearByItemDataModel(cityName = "Forest", R.drawable.forest))
        searcItemList.add(NearByItemDataModel(cityName = "Cafeteria", R.drawable.cafeteria))
        searcItemList.add(NearByItemDataModel(cityName = "Court", R.drawable.court))
        searcItemList.add(NearByItemDataModel(cityName = "Bakery", R.drawable.bakery))
        searcItemList.add(NearByItemDataModel(cityName = "Shop", R.drawable.shop))
        searcItemList.add(NearByItemDataModel(cityName = "Library", R.drawable.library))
        searcItemList.add(NearByItemDataModel(cityName = "Glass Shop", R.drawable.glass_shop))
        searcItemList.add(NearByItemDataModel(cityName = "GYM", R.drawable.gym))
        searcItemList.add(NearByItemDataModel(cityName = "Salon Shop", R.drawable.salon_shop))
        searcItemList.add(NearByItemDataModel(cityName = "Carpenter", R.drawable.carpenter))
        searcItemList.add(NearByItemDataModel(cityName = "Post Office", R.drawable.post_office))
        searcItemList.add(NearByItemDataModel(cityName = "Hardware", R.drawable.hardware))
        searcItemList.add(NearByItemDataModel(cityName = "Pharmacy", R.drawable.pharmacy))
        searcItemList.add(NearByItemDataModel(cityName = "School", R.drawable.university))
        searcItemList.add(NearByItemDataModel(cityName = "Ambulance", R.drawable.ambulance))
        searcItemList.add(NearByItemDataModel(cityName = "Water", R.drawable.water))
        searcItemList.add(NearByItemDataModel(cityName = "College", R.drawable.university))
        searcItemList.add(NearByItemDataModel(cityName = "University", R.drawable.university))
        searcItemList.add(NearByItemDataModel(cityName = "Painter", R.drawable.painter))
        searcItemList.add(NearByItemDataModel(cityName = "Cinema", R.drawable.cineam))
        searcItemList.add(NearByItemDataModel(cityName = "Jwwelery", R.drawable.jewelery))
        searcItemList.add(NearByItemDataModel(cityName = "Taxi", R.drawable.taxi))

        return searcItemList
    }

    companion object {
        val SELECTEDTYPE = "SELECTEDTYPE"
        val TAG = "NearbyFragmentTAG"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}