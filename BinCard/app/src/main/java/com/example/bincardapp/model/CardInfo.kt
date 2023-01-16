package com.example.bincardapp.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardInfo(
    var bin:String="",
    @SerializedName ("bank")
    val bank: Bank?,
    @SerializedName ( "brand")
    val brand: String,
    @SerializedName ( "country")
    val country: Country,
    @SerializedName ( "number")
    val number: Number?,
    @SerializedName ( "prepaid")
    val prepaid: Boolean?,
    @SerializedName ( "scheme")
    val scheme: String,
    @SerializedName ( "type")
    val type: String
):Parcelable