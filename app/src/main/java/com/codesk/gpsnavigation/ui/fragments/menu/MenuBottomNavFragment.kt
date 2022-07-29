package com.codesk.gpsnavigation.ui.fragments.menu

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.codesk.gpsnavigation.BuildConfig
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentMenuBottomNavigationBinding
import com.codesk.gpsnavigation.model.adapters.ChildAdapter
import com.codesk.gpsnavigation.model.adapters.SavedMapItemAdapter
import com.codesk.gpsnavigation.model.datamodels.SavedMapDataModel
import com.codesk.gpsnavigation.utill.commons.CommonFunctions.Companion.showDialog


class MenuBottomNavFragment : Fragment() {
    private var _binding: FragmentMenuBottomNavigationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MenuBottomNavViewModel by viewModels()


    private lateinit var savedMapItemAdapter: SavedMapItemAdapter
    var searcItemList = ArrayList<SavedMapDataModel>()

    var dbLatitude = 0.0
    var dbLongitude = 0.0

    val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP
        ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            //Remove swiped item from list and notify the RecyclerView
            val position = viewHolder.adapterPosition
            searcItemList.removeAt(position)
            savedMapItemAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBottomNavigationBinding.inflate(inflater, container, false)

        binding.apply {

            binding.headerLayoutsecond.image.visibility = View.INVISIBLE
            headerLayoutsecond.backImageview.setOnClickListener {
                findNavController().popBackStack(R.id.navigation_menu, true)
                findNavController().navigate(R.id.navigation_home)
            }

            savedMapItemAdapter = SavedMapItemAdapter(requireContext()) { latitude, lngitude ->
                dbLatitude = latitude
                dbLongitude = lngitude
                overlayLayout.visibility = View.VISIBLE
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

            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(rvSavedMap)

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
            if (list.isEmpty()) {
                val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.no_map_saved_dialouge)
                val btnCancel = dialog.findViewById(R.id.btn_save) as AppCompatButton
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
                binding.overlayLayout.visibility = View.VISIBLE
            }
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

    companion object {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}