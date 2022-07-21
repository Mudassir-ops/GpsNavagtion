package com.codesk.gpsnavigation.model.datamodels

data class FamousPlacesDetailDataModel(
    var cityName: String = "sss",
    var imageResource: Int,
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
)
