package com.codesk.gpsnavigation.data.remote



import com.codesk.gpsnavigation.data.remote.apimodels.ExampleJson2KtKotlin
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface ApiServices {



    @GET("mapbox.places/")
    fun getNearByAirport(
        @Url searchQueryName: String,
        @Query("access_token") access_token: String,
        @Query("proximity") proximity: String,
        @Query("limit") limit: String,
    ): Call<ExampleJson2KtKotlin?>?



}