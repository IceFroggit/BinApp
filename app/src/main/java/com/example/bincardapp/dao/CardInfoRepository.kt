package com.example.bincardapp.dao

import androidx.lifecycle.LiveData
import com.example.bincardapp.model.CardInfo

class CardInfoRepository(private val cardInfoDao: CardInfoDao) {
    val readAllData: LiveData<List<CardInfo>> = cardInfoDao.readAllData()
    suspend fun addCard(card: CardInfo){
        cardInfoDao.addCardInfo(card)
    }
}