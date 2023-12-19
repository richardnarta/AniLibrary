package com.example.anilibrary.model.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.anilibrary.model.data.util.Constants.ANIME_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeListDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(animeListEntity: AnimeListEntity)

    @Update
    suspend fun updateAnime(animeListEntity: AnimeListEntity)

    @Query("DELETE FROM $ANIME_TABLE WHERE id=:id")
    suspend fun deleteAnimeFromId(id:Int)

    @Query("SELECT * FROM $ANIME_TABLE WHERE anime_title LIKE :query ORDER BY anime_title ASC")
    fun getAllAnimeByQuery(query:String): Flow<List<AnimeListEntity>>

    @Query("SELECT * FROM $ANIME_TABLE WHERE list_type='watched' AND anime_title LIKE :query ORDER BY anime_title ASC")
    fun getWatchedAnimeByQuery(query:String): Flow<List<AnimeListEntity>>

    @Query("SELECT * FROM $ANIME_TABLE WHERE list_type='planned' AND anime_title LIKE :query ORDER BY anime_title ASC")
    fun getPlannedAnimeByQuery(query:String): Flow<List<AnimeListEntity>>

    @Query("SELECT EXISTS(SELECT 1 from $ANIME_TABLE WHERE id = :id)")
    suspend fun isAnimeAdded(id : Int) : Int

    @Query("SELECT COUNT(*) FROM $ANIME_TABLE")
    suspend fun countAllAnime(): Int

    @Query("SELECT COUNT(*) FROM $ANIME_TABLE WHERE list_type='watched'")
    suspend fun countWatchedAnime(): Int

    @Query("SELECT COUNT(*) FROM $ANIME_TABLE WHERE list_type='planned'")
    suspend fun countPlannedAnime(): Int
}