package com.codesk.gpsnavigation.ui.fragments.famousplacesmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.codesk.gpsnavigation.data.local.database.AppDatabase
import com.codesk.gpsnavigation.model.datamodels.SavedMapTable

class FamousPlaceMapFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private var mapDao = AppDatabase.getDatabase(application).savedMapDao()

    suspend fun insertSavedMap(savedMapTable: SavedMapTable) {
        mapDao.insertSavedMap(savedMapTable)
    }

}