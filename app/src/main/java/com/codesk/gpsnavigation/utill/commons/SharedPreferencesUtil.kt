package com.codesk.gpsnavigation.utill.commons

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class
SharedPreferencesUtil constructor(private var activity: Activity) {

    private val HomeSelected: String = "HomeSelected"

    fun saveHomeSelected(anim: Boolean) {
        val editor: SharedPreferences.Editor =
            activity.getSharedPreferences(HomeSelected, Context.MODE_PRIVATE).edit()
        editor.putBoolean("HomeSelected", anim)
        editor.apply()
    }

    fun getHomeSelected(): Boolean {
        val sharedPref = activity.getSharedPreferences(HomeSelected, Context.MODE_PRIVATE)
        return sharedPref.getBoolean("HomeSelected", false)
    }

}