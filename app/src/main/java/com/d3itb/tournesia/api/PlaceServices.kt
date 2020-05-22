package com.d3itb.tournesia.api

import com.d3itb.tournesia.data.remote.response.CityResponse
import com.d3itb.tournesia.data.remote.response.ProvinceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceServices {
    @GET("provinsi")
    fun getListProvince(): Call<ProvinceResponse>

    @GET("kota")
    fun getListCity(@Query("id_provinsi") idProvince: Int): Call<CityResponse>
}