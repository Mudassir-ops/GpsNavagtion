package com.codesk.gpsnavigation.ui.fragments.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codesk.gpsnavigation.databinding.FragmentSearchBottomNavigationBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentSearchBottomNavigationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBottomNavigationBinding.inflate(inflater, container, false)




        return binding.root
    }

    companion object {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}