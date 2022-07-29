package com.codesk.gpsnavigation.utill.commons

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class
SharedPreferencesUtil constructor(private var activity: Activity) {

    private val FamousPlacesSelected: String = "FamousPlacesSelected"
    private val ETATIME: String = "ETATIME"

    fun isFamousPlacesselected(anim: Boolean) {
        val editor: SharedPreferences.Editor =
            activity.getSharedPreferences(FamousPlacesSelected, Context.MODE_PRIVATE).edit()
        editor.putBoolean("FamousPlacesSelected", anim)
        editor.apply()
    }

    fun isFamousPlacesSelected(): Boolean {
        val sharedPref = activity.getSharedPreferences(FamousPlacesSelected, Context.MODE_PRIVATE)
        return sharedPref.getBoolean("FamousPlacesSelected", false)
    }

    fun saveETATime(etaTime: String) {
        val editor: SharedPreferences.Editor =
            activity.getSharedPreferences(ETATIME, Context.MODE_PRIVATE).edit()
        editor.putString("ETATIME", etaTime)
        editor.apply()
    }

    fun getETATime(): String {
        val sharedPref = activity.getSharedPreferences(ETATIME, Context.MODE_PRIVATE)
        return sharedPref.getString("ETATIME", "")!!
    }

}