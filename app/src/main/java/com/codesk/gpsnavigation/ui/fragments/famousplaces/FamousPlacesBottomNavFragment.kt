package com.codesk.gpsnavigation.ui.fragments.famousplaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codesk.gpsnavigation.databinding.FragmentFamousPlacesBottomNavigationBinding
import com.codesk.gpsnavigation.model.adapters.ParentAdapter
import com.codesk.gpsnavigation.model.datamodels.ParentDataFactory

class FamousPlacesBottomNavFragment : Fragment() {

    private var _binding: FragmentFamousPlacesBottomNavigationBinding? = null
    private val binding get() = _binding!!


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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        recyclerView = binding.rvParent

        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL, false
            )
            adapter = ParentAdapter(
                ParentDataFactory
                    .getParents(4)
            )
        }

    }
}