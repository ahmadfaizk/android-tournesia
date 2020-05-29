package com.d3itb.tournesia.model

data class Province(
    var id: Int,
    var name: String
) {
    override fun toString(): String {
        return name
    }
}