package com.example.bincardapp.dao
import androidx.room.TypeConverter
import com.example.bincardapp.model.Number
import com.example.bincardapp.model.Bank
import com.example.bincardapp.model.Country
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromNumber(number: Number):String?{
        return Gson().toJson(number)
    }

    @TypeConverter
    fun toNumber(json: String?): Number? {
        return Gson().fromJson(json, Number::class.java)
    }
    @TypeConverter
    fun fromBank(bank:Bank?):String?{
        return Gson().toJson(bank)
    }

    @TypeConverter
    fun toBank(json: String?): Bank? {
        return Gson().fromJson(json, Bank::class.java)
    }
    @TypeConverter
    fun fromCountry(country: Country?):String?{
        return Gson().toJson(country)
    }

    @TypeConverter
    fun toCountry(json: String?): Country? {
        return Gson().fromJson(json, Country::class.java)
    }
}