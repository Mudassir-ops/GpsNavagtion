package com.codesk.gpsnavigation.data.remote

import com.codesk.gpsnavigation.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class Networking {

    companion object {
        private const val NETWORK_CALL_TIMEOUT = 60
        private var retrofit: Retrofit? = null
        private val BASE_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/"

        fun getRetrofitInstance(): ApiServices? {
            if (retrofit == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .serializeNulls()
                    .create()
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(
                        OkHttpClient.Builder()
                            .addInterceptor(
                                HttpLoggingInterceptor()
                                    .apply {
                                        level = if (BuildConfig.DEBUG)
                                            HttpLoggingInterceptor.Level.BODY
                                        else
                                            HttpLoggingInterceptor.Level.NONE
                                    })
                            .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                            .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                            .build()
                    )
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit?.create(ApiServices::class.java)
        }

    }

}