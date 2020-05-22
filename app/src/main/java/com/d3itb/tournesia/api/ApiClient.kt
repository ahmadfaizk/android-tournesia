package com.d3itb.tournesia.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://tournesia.000webhostapp.com/api/"
    private const val BASE_URL_PLACE = "https://dev.farizdotid.com/api/daerahindonesia/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    val instance: Services = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(Services::class.java)

    val placeInstance: PlaceServices = Retrofit.Builder()
        .baseUrl(BASE_URL_PLACE)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(PlaceServices::class.java)
}