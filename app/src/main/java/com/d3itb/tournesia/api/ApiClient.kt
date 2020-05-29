package com.d3itb.tournesia.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://192.168.43.201:8000"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    val instance: Services = Retrofit.Builder()
        .baseUrl("$BASE_URL/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(Services::class.java)

    fun getImagePostUrl(name: String) = "$BASE_URL/posts/$name"
}