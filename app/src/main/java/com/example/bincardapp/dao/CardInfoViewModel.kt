package com.example.bincardapp.dao

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.bincardapp.model.CardInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardInfoViewModel(application:Application):AndroidViewModel(application) {
    val readAllData: LiveData<List<CardInfo>>
    private val repository:CardInfoRepository
    init{
        val cardInfoDao = CardInfoDatabase.getDatabase(application).cardInfoDao()
        repository = CardInfoRepository(cardInfoDao)
        readAllData = repository.readAllData
    }
    fun addCard(card: CardInfo){
        viewModelScope.launch(Dispatchers.IO){
            repository.addCard(card)
        }
    }
}