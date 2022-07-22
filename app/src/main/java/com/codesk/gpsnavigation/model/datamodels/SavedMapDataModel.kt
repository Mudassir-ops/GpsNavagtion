package com.codesk.gpsnavigation.model.datamodels

import com.google.gson.annotations.SerializedName

data class SavedMapDataModel(var cityName: String = "sss", var imageResource: Int, val savedPlaceLatitude: Double? =0.0 , val savedPlaceLongitude: Double? =0.0)
