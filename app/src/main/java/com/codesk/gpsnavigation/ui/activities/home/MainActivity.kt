package com.codesk.gpsnavigation.ui.activities.home


import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.ActivityMainBinding
import com.codesk.gpsnavigation.ui.fragments.search.SearchBottomNavFragment.Companion.RECORD_AUDIO_REQUEST_CODE
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                else -> hideBottomNav()
            }
        }

        binding.apply {
            navView.menu.getItem(0).isCheckable=false
            BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
                when (item.itemId) {

                    R.id.navigation_search -> {
                        item.isCheckable=true //here is the magic
                        navView.menu.getItem(0).isCheckable=true
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


            }

        }

    }


    private fun showBottomNav() {
        binding.navView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        binding.navView.visibility = View.GONE
    }


}