package com.d3itb.tournesia.data.remote.response

import com.d3itb.tournesia.model.Province
import com.google.gson.annotations.SerializedName

data class ProvinceResponse(
    @SerializedName("provinsi")
    val province: List<Province>
)