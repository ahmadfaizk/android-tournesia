package com.d3itb.tournesia.model

import com.google.gson.annotations.SerializedName

data class Post(
    var id: Int,

    var name: String,

    var description: String,

    @SerializedName("id_province")
    var idProvince: Int,

    var province: String,

    var address: String,

    @SerializedName("id_regency")
    var idRegency: Int,

    var regency: String,

    var votes: Float,

    @SerializedName("votes_count")
    var votesCount: Int,

    @SerializedName("id_category")
    var idCategory: Int,

    var category: String,

    @SerializedName("id_user")
    var idUser: Int,

    @SerializedName("user_name")
    var userName: String,

    @SerializedName("created_at")
    var createdAt: String,

    var images: List<Image>
)