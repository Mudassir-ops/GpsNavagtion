package com.codesk.gpsnavigation.ui.activities.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.data.remote.ApiServices
import com.codesk.gpsnavigation.data.remote.Networking
import com.codesk.gpsnavigation.databinding.ActivityMainBinding
import com.codesk.gpsnavigation.utill.commons.SharedPreferencesUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapbox.mapboxsdk.Mapbox


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
        super.onBackPressed()

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

}