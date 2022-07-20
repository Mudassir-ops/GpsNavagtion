package com.codesk.gpsnavigation.data.remote.apimodels

import com.google.gson.annotations.SerializedName


data class Context (

  @SerializedName("id"       ) var id       : String? = null,
  @SerializedName("wikidata" ) var wikidata : String? = null,
  @SerializedName("text"     ) var text     : String? = null

)