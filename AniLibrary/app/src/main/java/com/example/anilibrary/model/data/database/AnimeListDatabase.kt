package com.example.anilibrary.model.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.anilibrary.model.data.util.Constants

@Database(entities = [AnimeListEntity::class], version = 1, exportSchema = false)
abstract class AnimeListDatabase : RoomDatabase(){
    abstract fun getDao(): AnimeListDAO

    companion object {
        private var INSTANCE: AnimeListDatabase? = null
        fun getAnimeDB(context: Context): AnimeListDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimeListDatabase::class.java,
                    Constants.ANIME_DATABASE
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}