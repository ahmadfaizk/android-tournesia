package com.d3itb.tournesia.api

import com.d3itb.tournesia.data.remote.response.AuthResponse
import retrofit2.Call
import retrofit2.http.*

interface Services {
    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email: String,
              @Field("password") password: String): Call<AuthResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(@Field("name") name: String,
                 @Field("email") email: String,
                 @Field("password") password: String,
                 @Field("address") address: String): Call<AuthResponse>

    @GET("user")
    fun getUser(@Header("Authorization") token: String): Call<AuthResponse>
}