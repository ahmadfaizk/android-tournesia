package com.d3itb.tournesia.model

import com.google.gson.annotations.SerializedName

data class City(
    var id: Int,
    @SerializedName("id_provinsi")
    var idProvince: String,
    @SerializedName("nama")
    var name: String
) {
    override fun toString(): String {
        return name
    }
}