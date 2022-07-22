package com.codesk.gpsnavigation.ui.activities.home


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isGone
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.codesk.gpsnavigation.R
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.databinding.ActivityMainBinding
import com.codesk.gpsnavigation.utill.commons.SharedPreferencesUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapbox.mapboxsdk.Mapbox
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MianActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        val navController =
            Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
        setupWithNavController(binding.navView, navController)



        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.e("destinationID", "onDestinationChanged: " + destination.id);
            when (destination.id) {
                R.id.navigation_search -> {
                    SharedPreferencesUtil(this@MainActivity).saveHomeSelected(true)
                    showBottomNav()
                }
                R.id.navigation_nearby -> {
                    showBottomNav()
                }
                R.id.navigation_famousplaces -> {
                    showBottomNav()
                }
                R.id.navigation_menu -> {
                    showBottomNav()
                }
                R.id.navigation_home -> {
                    showBottomNav()
                }
                else -> hideBottomNav()
            }
        }

        binding.apply {


            deselectAllItems()

            /*     BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
                     when (item.itemId) {

                         R.id.navigation_search -> {
                             item.isCheckable = true //here is the magic
                             navView.menu.getItem(0).isCheckable = true
                             //notify the listener
                             return@OnNavigationItemSelectedListener true
                         }
                         R.id.navigation_nearby ->{
                             item.isCheckable=true
                             //notify the listener
                             return@OnNavigationItemSelectedListener true
                         }
                         R.id.navigation_famousplaces ->{
                             //go to forgot user fragment
                             item.isCheckable=true

                             //notify the listener
                             return@OnNavigationItemSelectedListener true
                         }
                         R.id.navigation_menu ->{
                             //go to forgot user fragment
                             item.isCheckable=true

                             //notify the listener
                             return@OnNavigationItemSelectedListener true
                         }

                         else -> false
                     }


                 }*/

        }

    }

    private fun showBottomNav() {
        binding.navView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        binding.navView.visibility = View.GONE
    }

    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        val id = navController.currentDestination!!.id

        if (id == R.id.navigation_home) {
            this@MainActivity.showDialog(
                title = "Are You want to close App ?",
                description = resources.getString(R.string.app_name),
                titleOfPositiveButton = "Exit",
                titleOfNegativeButton = "Cancel",
                positiveButtonFunction = {
                    exitProcess(0)
                })

        } else if(id==R.id.navigation_search_place_map) {
            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_home)
        }else{
            super.onBackPressed()
        }


        SharedPreferencesUtil(this@MainActivity).saveHomeSelected(false)
    }



    @SuppressLint("RestrictedApi")
    fun BottomNavigationView.deselectAllItems() {

        val menu = binding.navView.menu

        for (i in 0 until menu.size()) {
            (menu.getItem(i) as? MenuItemImpl)?.let {
                it.isExclusiveCheckable = false
                it.isChecked = false
                it.isExclusiveCheckable = true
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun deselectAllItems() {
        val menu = binding.navView.menu
        for (i in 0 until menu.size()) {
            (menu.getItem(i) as? MenuItemImpl)?.let {
                it.isExclusiveCheckable = false
                it.isChecked = false
                it.isExclusiveCheckable = true
            }
        }
    }

    fun Context.showDialog(
        title: String,
        description: String,
        titleOfPositiveButton: String? = null,
        titleOfNegativeButton: String? = null,
        positiveButtonFunction: (() -> Unit)? = null,
        negativeButtonFunction: (() -> Unit)? = null,
        likeButtonFunction: (() -> Unit)? = null,
    ) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_exit_dialouge)
        val dialogTitle = dialog.findViewById(R.id.tv_privacy) as AppCompatTextView
        val dialogCloseButton = dialog.findViewById(R.id.exit_btn) as AppCompatButton
        val dialogPositiveButton = dialog.findViewById(R.id.cancel_btn) as AppCompatButton
        val dialogLikeButton = dialog.findViewById(R.id.btn_rate) as AppCompatButton

        dialogTitle.text = title

        titleOfPositiveButton?.let { dialogPositiveButton.text = it } ?: dialogPositiveButton.isGone
        titleOfNegativeButton?.let { dialogCloseButton.text = it } ?: dialogCloseButton.isGone

        dialogPositiveButton.setOnClickListener {
            positiveButtonFunction?.invoke()
            dialog.dismiss()

        }
        dialogCloseButton.setOnClickListener {
            negativeButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialogLikeButton.setOnClickListener {
            likeButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

}