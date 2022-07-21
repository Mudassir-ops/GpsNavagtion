package com.codesk.gpsnavigation.ui.fragments.menu

import android.app.Application
import androidx.lifecycle.*
import com.codesk.gpsnavigation.data.local.database.AppDatabase
import com.codesk.gpsnavigation.model.datamodels.SavedMapTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuBottomNavViewModel(application: Application) : AndroidViewModel(application) {

    private var mapDao = AppDatabase.getDatabase(application).savedMapDao()

    private val _savedMapData: MutableLiveData<List<SavedMapTable>> = MutableLiveData()
    val savedMapdata: LiveData<List<SavedMapTable>>
        get() = _savedMapData


    fun getAllSavedMap(): LiveData<List<SavedMapTable>> {
        val dta = viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                _savedMapData.value = mapDao.getAllSavedMap()
            }
        }
        return savedMapdata
    }


}