package com.codesk.gpsnavigation.ui.activities.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.codesk.gpsnavigation.data.remote.ApiServices
import com.codesk.gpsnavigation.data.remote.Networking
import com.codesk.gpsnavigation.data.remote.apimodels.ExampleJson2KtKotlin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MianActivityViewModel(application: Application) : AndroidViewModel(application) {



    fun callGenerateTokenApi() {
        val standardService: ApiServices? = Networking.getRetrofitInstance()
        standardService?.getNearByAirport(
            searchQueryName = "airport.json",
            access_token = "pk.eyJ1Ijoic2hhaHphZGtoYW4xMjMiLCJhIjoiY2sweG16eGtiMDdyMzNjbzE1eGg2cjV2biJ9.ru-Gf4mnyj7N_TV93QFX4Q",
            proximity = "72.94574397437496,33.61620668993463",
            limit = "5"
        )?.
        enqueue(object :
            Callback<ExampleJson2KtKotlin?> {
            override fun onResponse(
                call: Call<ExampleJson2KtKotlin?>?,
                response: Response<ExampleJson2KtKotlin?>
            ) {
                if (response.isSuccessful) {
                    Log.d("SSSS", "onResponse: This called2${response.body()}")
                } else {

                    Log.d("SSSS", "onResponse: This called2")
                }
            }

            override fun onFailure(call: Call<ExampleJson2KtKotlin?>?, t: Throwable?) {

                Log.d("SSSS", "onResponse: This sadadasd as dsad asd22")
                // newsData.setValue(null)
            }
        })
    }


}