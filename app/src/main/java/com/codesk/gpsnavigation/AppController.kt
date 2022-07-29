package com.codesk.gpsnavigation

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class AppController : MultiDexApplication() {
    companion object {
        private var instance: AppController? = null

        fun applicationContext(): AppController {
            return instance as AppController
        }
    }
    protected override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    @Synchronized
    fun getInstance(): AppController {
        return instance!!
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    init {
        instance = this
    }

}