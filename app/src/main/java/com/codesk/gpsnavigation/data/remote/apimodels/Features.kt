package com.codesk.gpsnavigation.data.remote.apimodels

import com.google.gson.annotations.SerializedName


data class Features (

  @SerializedName("id"         ) var id         : String?            = null,
  @SerializedName("type"       ) var type       : String?            = null,
  @SerializedName("place_type" ) var placeType  : ArrayList<String>  = arrayListOf(),
  @SerializedName("relevance"  ) var relevance  : Int?               = null,
  @SerializedName("properties" ) var properties : Properties?        = Properties(),
  @SerializedName("text"       ) var text       : String?            = null,
  @SerializedName("place_name" ) var placeName  : String?            = null,
  @SerializedName("center"     ) var center     : ArrayList<Double>  = arrayListOf(),
  @SerializedName("geometry"   ) var geometry   : Geometry?          = Geometry(),
  @SerializedName("context"    ) var context    : ArrayList<Context> = arrayListOf()

)