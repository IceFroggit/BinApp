package com.example.bincardapp.model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "card_table")
data class CardInfo(
    @PrimaryKey
    var bin:String="",
    @SerializedName ("bank")
    var bank: Bank?,
    @SerializedName ( "brand")
    var brand: String?,
    @SerializedName ( "country")
    val country: Country,
    @SerializedName ( "number")
    var number: Number?,
    @SerializedName ( "prepaid")
    var prepaid: Boolean?,
    @SerializedName ( "scheme")
    val scheme: String,
    @SerializedName ( "type")
    var type: String
):Parcelable