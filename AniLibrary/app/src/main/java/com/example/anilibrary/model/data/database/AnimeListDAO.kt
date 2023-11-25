package com.example.anilibrary.model.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.anilibrary.model.data.util.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeListDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(animeListEntity: AnimeListEntity)

    @Update
    suspend fun updateAnime(animeListEntity: AnimeListEntity)

    @Delete
    suspend fun deleteAnime(animeListEntity: AnimeListEntity)

    @Query("DELETE FROM ${Constants.ANIME_TABLE}")
    suspend fun deleteAll()

    @Query("SELECT * FROM ${Constants.ANIME_TABLE} ORDER BY anime_title DESC")
    fun getAllAnime(): Flow<List<AnimeListEntity>>

    @Query("SELECT * FROM ${Constants.ANIME_TABLE} WHERE list_type='watched' ORDER BY anime_title DESC")
    fun getWatchedAnime(): Flow<List<AnimeListEntity>>

    @Query("SELECT * FROM ${Constants.ANIME_TABLE} WHERE list_type='planned' ORDER BY anime_title DESC")
    fun getPlannedAnime(): Flow<List<AnimeListEntity>>

    @Query("DELETE FROM ${Constants.ANIME_TABLE} WHERE id=:id")
    fun deleteAnimeFromId(id:Int)
}