package com.d3itb.tournesia.model

data class Category(
    var id: Int,
    var name: String
) {
    override fun toString(): String {
        return name
    }
}