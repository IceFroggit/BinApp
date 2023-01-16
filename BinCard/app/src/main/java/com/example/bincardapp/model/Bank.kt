package com.example.bincardapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bank(
    val city: String,
    val name: String,
    val phone: String,
    val url: String
):Parcelable