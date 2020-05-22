package com.d3itb.tournesia.data.remote.response

import com.d3itb.tournesia.model.City
import com.google.gson.annotations.SerializedName

data class CityResponse(
    @SerializedName("kota_kabupaten")
    var city: List<City>
)