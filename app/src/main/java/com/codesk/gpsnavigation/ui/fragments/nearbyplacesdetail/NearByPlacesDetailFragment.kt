package com.codesk.gpsnavigation.ui.fragments.nearbyplacesdetail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
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
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NearByPlacesDetailFragment : Fragment() {

    private val viewModel: NearbyPlacesDetailViewModel by viewModels()

    private var _binding: FragmentNearByPlacesDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var nearByPlacesDetailItemAdapter: NearByPlacesDetailItemAdapter
    var searcItemList = ArrayList<NearByPlacesDetailDataModel>()

    var selectedTypeName = ""
    var SELECTEDPPOSITION = -1
    var SELECTEDImageResId = -1


    var latitude = 0.0
    var longitude = 0.0
    var palce = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token));
        _binding = FragmentNearByPlacesDetailBinding.inflate(inflater, container, false)
        selectedTypeName = arguments?.getString(NearbyBottomNavFragment.SELECTEDTYPE, "")!!
        SELECTEDPPOSITION = arguments?.getInt(NearbyBottomNavFragment.SELECTEDPPOSITION, -1)!!
        SELECTEDImageResId = arguments?.getInt(NearbyBottomNavFragment.SELECTEDIAMGERESID, -1)!!

        viewModel.showProgressBar.value = true
        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.circularProgressIndicatr.visibility = View.VISIBLE
            } else {
                binding.circularProgressIndicatr.visibility = View.GONE
            }
        })

        val mapboxGeocoding = MapboxGeocoding.builder()
            .accessToken(resources.getString(R.string.mapbox_access_token))
            .query(selectedTypeName)
            .proximity(
                Point.fromLngLat(
                    AppConstants.mCurrentLocation!!.longitude,
                    AppConstants.mCurrentLocation!!.latitude
                )
            )
            .build()

        mapboxGeocoding.enqueueCall(object : Callback<GeocodingResponse> {
            override fun onResponse(
                call: Call<GeocodingResponse>,
                response: Response<GeocodingResponse>
            ) {

                viewModel.showProgressBar.value = false
                val results = response.body()!!.features()
                if (results.size > 0) {
                    setApiData(results, SELECTEDImageResId)
                } else {
                    val dialog = Dialog(requireActivity(), R.style.Theme_Dialog)
                    dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setCancelable(true)
                    dialog.setContentView(R.layout.no_map_saved_dialouge)
                    val btnCancel = dialog.findViewById(R.id.btn_save) as AppCompatButton
                    val tvText = dialog.findViewById(R.id.edt_save_title) as AppCompatTextView
                    tvText.text = "NO Matching Result Found"
                    btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()

                }
            }

            override fun onFailure(call: Call<GeocodingResponse>, throwable: Throwable) {
                val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.no_map_saved_dialouge)
                val btnCancel = dialog.findViewById(R.id.btn_save) as AppCompatButton
                val tvText = dialog.findViewById(R.id.edt_save_title) as AppCompatTextView
                tvText.text = "failed to connect to api.mapbox.com \n" +
                        " Check Internet Stability"
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
                viewModel.showProgressBar.value = false
                throwable.printStackTrace()
            }
        })
        binding.apply {
            headerLayoutsecond.labelSavedMap.text = selectedTypeName
            headerLayoutsecond.backImageview.setOnClickListener {
                findNavController().popBackStack(R.id.navigation_nearby_place_detail, true)
                findNavController().navigate(R.id.navigation_nearby)
            }

            headerLayoutsecond.image.setOnClickListener {
                findNavController().popBackStack(R.id.navigation_nearby_place_detail, true)
                findNavController().popBackStack(R.id.navigation_nearby, true)
                findNavController().navigate(R.id.navigation_menu)
            }
            binding.showOnTheMapLayoutRight.setOnClickListener {
                val bundle = Bundle()
                bundle.putDouble(SelectedLatitude, latitude)
                bundle.putDouble(SelectedLngitude, longitude)
                bundle.putString(SelectedPlaceNAME, palce)
                findNavController().navigate(R.id.navigation_nearby_places_map, bundle)
            }

            nearByPlacesDetailItemAdapter =
                NearByPlacesDetailItemAdapter(requireContext()) { pos, lat, lng, placeName ->
                    latitude = lat
                    longitude = lng
                    palce = placeName
                    binding.tvPlaceName.text = placeName
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
        _binding = null
    }

    companion object {
        val SelectedLatitude = "SelectedLatitude"
        val SelectedLngitude = "SelectedLngitude"
        val SelectedPlaceNAME = "SelectedPlaceNAME"
        const val TAG = "NearByPlaceDetailFrag"
    }

    fun setApiData(results: MutableList<CarmenFeature>, imageResId: Int) {
        searcItemList.clear()
        results.map {
            val data = it.properties()?.getAsJsonPrimitive("address")
            if (data == null) {
                searcItemList.add(
                    NearByPlacesDetailDataModel(
                        cityName = it.placeName()!!,
                        imageResId, it.placeName()!!,
                        it.center()!!.latitude(),
                        it.center()!!.longitude()
                    )
                )
            } else {
                searcItemList.add(
                    NearByPlacesDetailDataModel(
                        cityName = it.placeName()!!,
                        imageResId,
                        data.asString,
                        it.center()!!.latitude(),
                        it.center()!!.longitude()
                    )
                )
            }
        }
        nearByPlacesDetailItemAdapter.setNearByPlacesDeatilitemList(searcItemList)
    }
}