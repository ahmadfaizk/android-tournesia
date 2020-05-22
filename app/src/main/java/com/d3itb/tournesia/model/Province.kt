package com.d3itb.tournesia.model

import com.google.gson.annotations.SerializedName

data class Province(
    var id: Int,
    @SerializedName("nama")
    var name: String
) {
    override fun toString(): String {
        return name
    }
}