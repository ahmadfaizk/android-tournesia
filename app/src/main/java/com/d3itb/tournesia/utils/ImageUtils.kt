package com.d3itb.tournesia.utils

object ImageUtils {
    private const val baseUrl = "http://192.168.43.201:8000/"

    fun getImagePostUrl(name: String) : String = "$baseUrl/posts/$name"

}