package com.example.bincardapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Number(
    val length: Int,
    val luhn: Boolean
):Parcelable