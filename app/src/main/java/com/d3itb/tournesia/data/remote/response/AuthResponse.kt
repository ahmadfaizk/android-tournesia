package com.d3itb.tournesia.data.remote.response

import com.d3itb.tournesia.model.Token
import com.d3itb.tournesia.model.User

data class AuthResponse(
    var error: Boolean,
    var message: String,
    var errors_detail: User? = null,
    var data: Token? = null
)