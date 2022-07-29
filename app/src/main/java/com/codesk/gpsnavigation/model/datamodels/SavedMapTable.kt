package com.codesk.gpsnavigation.model.datamodels

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "saved_map_table")
data class SavedMapTable(

    @PrimaryKey
    @NonNull
    @SerializedName("savedPlaceName")
    val savedPlaceName: String,

    @SerializedName("savedPlaceLatitude")
    val savedPlaceLatitude: Double? =33.516102992887575 ,

    @SerializedName("savedPlaceLongitude")
    val savedPlaceLongitude: Double? =73.15433262324264,

    )