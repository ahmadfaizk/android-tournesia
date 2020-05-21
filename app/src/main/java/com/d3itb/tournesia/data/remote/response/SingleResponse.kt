package com.d3itb.tournesia.data.remote.response

data class SingleResponse<T>(
    var error: Boolean,
    var message: String,
    var data: T
)