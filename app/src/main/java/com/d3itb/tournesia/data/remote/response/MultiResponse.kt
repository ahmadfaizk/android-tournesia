package com.d3itb.tournesia.data.remote.response

data class MultiResponse<T>(
    var error: Boolean,
    var message: String,
    var data: List<T>
)