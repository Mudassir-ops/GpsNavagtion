package com.codesk.gpsnavigation.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codesk.gpsnavigation.model.datamodels.SavedMapTable

@Dao
interface SavedMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedMap(savedMapTable: SavedMapTable)

    @Query("SELECT * FROM saved_map_table")
    suspend fun getAllSavedMap(): List<SavedMapTable>

}