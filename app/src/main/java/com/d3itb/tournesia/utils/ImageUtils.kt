package com.d3itb.tournesia.utils

object ImageUtils {
    private const val baseUrl = "https://tournesia.000webhostapp.com"

    fun getImagePostUrl(name: String) : String = "$baseUrl/posts/$name"

}