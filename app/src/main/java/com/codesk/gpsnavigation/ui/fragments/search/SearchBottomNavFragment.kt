package com.codesk.gpsnavigation.ui.fragments.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codesk.gpsnavigation.databinding.FragmentSearchBottomNavigationBinding
import com.codesk.gpsnavigation.model.adapters.SearchItemAdapter
import com.codesk.gpsnavigation.model.datamodels.SearchItemDataModel
import java.util.*

class SearchBottomNavFragment : Fragment() {

    private var _binding: FragmentSearchBottomNavigationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterSearchItemAdapter: SearchItemAdapter
    var searcItemList = ArrayList<SearchItemDataModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBottomNavigationBinding.inflate(inflater, container, false)
        binding.headerLayout.backImageview.visibility = View.VISIBLE

        binding.apply {
            adapterSearchItemAdapter = SearchItemAdapter(requireContext()) {}
            rvSearch.apply {
                adapter = adapterSearchItemAdapter
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
                    adapterSearchItemAdapter.filter(text)

                }
            })
            btnRecentSearch.setOnClickListener {
                adapterSearchItemAdapter.setSearchitemList(gerSearchData())
            }
            btnClearSearch.setOnClickListener {
                searcItemList.clear()
                adapterSearchItemAdapter.setSearchitemList(searcItemList)
            }

            headerLayout.backImageview.setOnClickListener {
                //------initial fragment
            }

        }
        adapterSearchItemAdapter.setSearchitemList(gerSearchData())
        return binding.root
    }

    private fun gerSearchData(): ArrayList<SearchItemDataModel> {
        searcItemList.clear()
        searcItemList.add(SearchItemDataModel(cityName = "Rawalpindi"))
        searcItemList.add(SearchItemDataModel(cityName = "Islamabad"))
        searcItemList.add(SearchItemDataModel(cityName = "Lahore"))
        searcItemList.add(SearchItemDataModel(cityName = "Karachi"))
        searcItemList.add(SearchItemDataModel(cityName = "Faislabad"))

        return searcItemList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}