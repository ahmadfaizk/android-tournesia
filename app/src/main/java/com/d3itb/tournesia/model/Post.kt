package com.d3itb.tournesia.model

import com.google.gson.annotations.SerializedName

data class Post(
    var id: Int,

    var name: String,

    var description: String,

    var province: String,

    var address: String,

    var regency: String,

    var votes: Float,

    @SerializedName("votes_count")
    var votesCount: Int,

    var category: String,

    @SerializedName("id_user")
    var idUser: Int,

    @SerializedName("user_name")
    var userName: String,

    @SerializedName("created_at")
    var createdAt: String,

    var images: List<Image>
)