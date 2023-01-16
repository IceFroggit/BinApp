package com.example.bincardapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bank(
    var city: String,
    var name: String,
    var phone: String,
    var url: String
):Parcelable