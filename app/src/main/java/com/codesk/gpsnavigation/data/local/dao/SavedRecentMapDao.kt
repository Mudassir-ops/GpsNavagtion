package com.codesk.gpsnavigation.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codesk.gpsnavigation.model.datamodels.SavedRecentMapTable

@Dao
interface SavedRecentMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentMap(savedRecentMapTable: SavedRecentMapTable)

    @Query("SELECT * FROM saved_recent_map_table")
    suspend fun getAllRecentMap(): List<SavedRecentMapTable>

    @Query("DELETE FROM saved_recent_map_table")
    suspend fun deleteRecentMapTable()

    @Query("DELETE FROM saved_recent_map_table WHERE savedPlaceID = :savedPlaceID")
    fun removeRowAtPosition(savedPlaceID: Int)

}