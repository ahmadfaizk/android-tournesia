package com.d3itb.tournesia.model

data class User(
    var id: Int,
    var name: String,
    var email: String,
    var address: String,
    var photo: String? = null,
    var password: String? = null
)