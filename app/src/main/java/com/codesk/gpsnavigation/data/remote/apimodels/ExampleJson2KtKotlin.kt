package com.codesk.gpsnavigation.data.remote.apimodels

import com.google.gson.annotations.SerializedName


data class ExampleJson2KtKotlin (

  @SerializedName("type"        ) var type        : String?             = null,
  @SerializedName("query"       ) var query       : ArrayList<String>   = arrayListOf(),
  @SerializedName("features"    ) var features    : ArrayList<Features> = arrayListOf(),
  @SerializedName("attribution" ) var attribution : String?             = null

)