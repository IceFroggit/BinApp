package com.example.bincardapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bincardapp.model.CardInfo

@Dao
interface CardInfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCardInfo(card: CardInfo)

    @Query("SELECT * FROM card_table ORDER BY bin ASC")
    fun readAllData():LiveData<List<CardInfo>>
}