package com.codesk.gpsnavigation.data.local.dao


import androidx.room.*
import com.codesk.gpsnavigation.model.datamodels.SavedRecentMapTable

@Dao
interface SavedRecentMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentMap(savedRecentMapTable: SavedRecentMapTable)

    @Query("SELECT * FROM saved_recent_map_table")
    suspend fun getAllRecentMap(): List<SavedRecentMapTable>

    @Query("DELETE FROM saved_recent_map_table")
    suspend fun deleteRecentMapTable()

    @Query("UPDATE saved_recent_map_table SET savedPlaceName=:savedPlaceName,savedPlaceLatitude=:savedPlaceLatitude,savedPlaceLongitude =:savedPlaceLongitude WHERE savedPlaceID = :savedPlaceID")
    fun updateRecentMap(savedPlaceName: String?,savedPlaceLatitude: Double?,savedPlaceLongitude: Double?, savedPlaceID: Int)

//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    fun updateProduct(savedRecentMapTable: SavedRecentMapTable)


}