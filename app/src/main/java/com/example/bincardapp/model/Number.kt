package com.example.bincardapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Number(
    var length: Int,
    var luhn: Boolean
):Parcelable