package com.example.bincardapp.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bincardapp.model.CardInfo

@Database(entities = [CardInfo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CardInfoDatabase:RoomDatabase(){
    abstract fun cardInfoDao(): CardInfoDao
    companion object{
        @Volatile
        private var INSTANCE: CardInfoDatabase? = null

        fun getDatabase(context:Context): CardInfoDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardInfoDatabase::class.java,
                    "cardinfo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}