package com.d3itb.tournesia.model

import com.google.gson.annotations.SerializedName

data class Regency(
    var id: Int,
    @SerializedName("id_province")
    var idProvince: String,

    var name: String
) {
    override fun toString(): String {
        return name
    }
}