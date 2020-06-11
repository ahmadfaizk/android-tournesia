package com.d3itb.tournesia.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    var id: Int,
    var name: String
): Parcelable