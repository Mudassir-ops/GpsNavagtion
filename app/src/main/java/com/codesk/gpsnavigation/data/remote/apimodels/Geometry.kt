package com.codesk.gpsnavigation.data.remote.apimodels

import com.google.gson.annotations.SerializedName


data class Geometry (

  @SerializedName("coordinates" ) var coordinates : ArrayList<Double> = arrayListOf(),
  @SerializedName("type"        ) var type        : String?           = null

)