package com.d3itb.tournesia.api

import com.d3itb.tournesia.data.remote.response.AuthResponse
import com.d3itb.tournesia.data.remote.response.MultiResponse
import com.d3itb.tournesia.data.remote.response.SingleResponse
import com.d3itb.tournesia.model.*
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

    @GET("post/{id}")
    fun getPostById(@Header("Authorization") token: String,
                    @Path("id") id: Int): Call<SingleResponse<Post>>

    @GET("post/search")
    fun searchPost(@Header("Authorization") token: String,
                   @Query("name") name: String): Call<MultiResponse<Post>>

    @GET("category")
    fun getCategory(@Header("Authorization") token: String): Call<MultiResponse<Category>>

    @Multipart
    @POST("post/add")
    fun createPost(@Header("Authorization") token: String,
                   @Part images: MultipartBody.Part,
                   @PartMap params: HashMap<String, RequestBody>): Call<SingleResponse<Post>>

    @Multipart
    @POST("post/{id}/update")
    fun updatePost(@Header("Authorization") token: String,
                   @Path("id") idPost: Int,
                   @Part images: MultipartBody.Part?,
                   @PartMap params: HashMap<String, RequestBody>): Call<SingleResponse<Post>>

    @GET("post/{id}/comment/all")
    fun getAllCommentByIdPost(@Header("Authorization") token: String,
                           @Path("id") idPost: Int): Call<MultiResponse<Comment>>

    @GET("post/{id}/comment")
    fun getMyCommentByIdPost(@Header("Authorization") token: String,
                              @Path("id") idPost: Int): Call<SingleResponse<Comment>>

    @Multipart
    @POST("post/{id}/comment/add")
    fun createComment(@Header("Authorization") token: String,
                      @Path("id") idPost: Int,
                      @Part images: MultipartBody.Part?,
                      @PartMap params: HashMap<String, RequestBody>): Call<SingleResponse<Comment>>

    @Multipart
    @POST("post/{id}/comment/update")
    fun updateComment(@Header("Authorization") token: String,
                      @Path("id") idPost: Int,
                      @Part images: MultipartBody.Part?,
                      @PartMap params: HashMap<String, RequestBody>): Call<SingleResponse<Comment>>

    @GET("post/{id}/comment/delete")
    fun deleteComment(@Header("Authorization") token: String,
                      @Path("id") idPost: Int): Call<SingleResponse<Comment>>

    @GET("post/{id}/delete")
    fun deletePost(@Header("Authorization") token: String,
                   @Path("id") idPost: Int): Call<SingleResponse<Post>>

    @GET("province")
    fun getProvinces(): Call<MultiResponse<Province>>

    @GET("province/{id}")
    fun getRegencies(@Path("id") idProvince: Int): Call<MultiResponse<Regency>>
}