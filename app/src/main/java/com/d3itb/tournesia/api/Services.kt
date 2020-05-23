package com.d3itb.tournesia.api

import com.d3itb.tournesia.data.remote.response.AuthResponse
import com.d3itb.tournesia.data.remote.response.MultiResponse
import com.d3itb.tournesia.data.remote.response.SingleResponse
import com.d3itb.tournesia.model.Category
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    fun getUser(@Header("Authorization") token: String): Call<SingleResponse<User>>

    @GET("post/all")
    fun getAllPost(@Header("Authorization") token: String): Call<MultiResponse<Post>>

    @GET("post")
    fun getPostByMe(@Header("Authorization") token: String): Call<MultiResponse<Post>>

    @GET("category")
    fun getCategory(@Header("Authorization") token: String): Call<MultiResponse<Category>>

    @Multipart
    @POST("post/add")
    fun createPost(@Header("Authorization") token: String,
                   @Part images: MultipartBody.Part,
                   @PartMap params: HashMap<String, RequestBody>): Call<SingleResponse<Post>>
}