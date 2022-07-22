package com.codesk.gpsnavigation.ui.fragments.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codesk.gpsnavigation.data.local.database.AppDatabase
import com.codesk.gpsnavigation.model.datamodels.SavedRecentMapTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchBottomNavViewModel(application: Application) : AndroidViewModel(application) {
    private var mapDao = AppDatabase.getDatabase(application).savedRecentMapDao()

    private val _savedMapData: MutableLiveData<List<SavedRecentMapTable>> = MutableLiveData()
    val savedMapdata: LiveData<List<SavedRecentMapTable>>
        get() = _savedMapData


    fun getAllRecentMap(): LiveData<List<SavedRecentMapTable>> {
        val dta = viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _savedMapData.value = mapDao.getAllRecentMap()
            }
        }
        return savedMapdata
    }

    suspend fun insertRecentMap(savedRecentMapTable: SavedRecentMapTable) {
        mapDao.insertRecentMap(savedRecentMapTable)
    }

     fun updateRecentMap(
        savedPlaceName: String,
        savedPlaceLatitude: Double,
        savedPlaceLongitude: Double,
        savedPlaceID: Int
    ) {
         viewModelScope.launch(Dispatchers.IO) {
             mapDao.updateRecentMap(
                 savedPlaceName = savedPlaceName,
                 savedPlaceLatitude = savedPlaceLatitude,
                 savedPlaceLongitude = savedPlaceLongitude,
                 savedPlaceID = savedPlaceID
             )
         }
    }

    suspend fun deleteRecentMapTable() {
        mapDao.deleteRecentMapTable()
    }

}