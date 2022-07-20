package com.codesk.gpsnavigation.data.remote.apimodels
import com.google.gson.annotations.SerializedName

data class Properties (

  @SerializedName("wikidata"   ) var wikidata   : String?  = null,
  @SerializedName("category"   ) var category   : String?  = null,
  @SerializedName("landmark"   ) var landmark   : Boolean? = null,
  @SerializedName("address"    ) var address    : String?  = null,
  @SerializedName("foursquare" ) var foursquare : String?  = null,
  @SerializedName("maki"       ) var maki       : String?  = null

)