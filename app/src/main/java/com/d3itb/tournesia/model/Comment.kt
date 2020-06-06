package com.d3itb.tournesia.model

import com.google.gson.annotations.SerializedName

data class Comment(
    val id: Int,
    val votes: Int,
    val comment: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("created_at")
    val createdAt: String,
    val images: List<Image>
)