package com.codesk.gpsnavigation.ui.fragments.famousplaces

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentFamousPlacesBottomNavigationBinding
import com.codesk.gpsnavigation.model.adapters.ParentAdapter
import com.codesk.gpsnavigation.model.datamodels.ParentDataFactory
import com.codesk.gpsnavigation.model.datamodels.ParentModel
import java.util.*

class FamousPlacesBottomNavFragment : Fragment() {

    private var _binding: FragmentFamousPlacesBottomNavigationBinding? = null
    private val binding get() = _binding!!

    private lateinit var parentAdapter: ParentAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val famousPlacesBottomNavViewModel =
            ViewModelProvider(this).get(FamousPlacesBottomNavViewModel::class.java)
        _binding = FragmentFamousPlacesBottomNavigationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initRecycler()


        binding.apply {
            headerLayout.backImageview.setOnClickListener {
                findNavController().popBackStack(R.id.navigation_famousplaces, true)
                findNavController().navigate(R.id.navigation_home)
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
                    parentAdapter.filter(text)

                }
            })
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun initRecycler() {
        recyclerView = binding.rvParent
         parentAdapter=ParentAdapter(requireContext()){
           findNavController().navigate(R.id.navigation_famous_places_detail)
         }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = parentAdapter
        }

        parentAdapter.setSearchitemList(ParentDataFactory.getParents(5) as ArrayList<ParentModel>)

    }
}