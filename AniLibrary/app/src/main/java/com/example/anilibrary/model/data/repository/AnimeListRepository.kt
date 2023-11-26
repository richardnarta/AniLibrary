package com.example.anilibrary.model.data.repository

import androidx.annotation.WorkerThread
import com.example.anilibrary.model.data.database.AnimeListDAO
import com.example.anilibrary.model.data.database.AnimeListEntity
import kotlinx.coroutines.flow.Flow

class AnimeListRepository(private val animeDAO: AnimeListDAO) {
    fun getAllAnimeByQuery(query: String): Flow<List<AnimeListEntity>> {
        return animeDAO.getAllAnimeByQuery(query)
    }

    fun getWatchedAnimeByQuery(query: String): Flow<List<AnimeListEntity>> {
        return animeDAO.getWatchedAnimeByQuery(query)
    }

    fun getPlannedAnimeByQuery(query: String): Flow<List<AnimeListEntity>> {
        return animeDAO.getPlannedAnimeByQuery(query)
    }

    @WorkerThread
    suspend fun insertAnime(animeData: AnimeListEntity){
        animeDAO.insertAnime(animeData)
    }

    @WorkerThread
    suspend fun deleteAnimeFromId(id: Int){
        animeDAO.deleteAnimeFromId(id)
    }

    @WorkerThread
    suspend fun isAnimeAdded(id: Int):Int{
        return animeDAO.isAnimeAdded(id)
    }

    @WorkerThread
    suspend fun countAllAnime():Int{
        return animeDAO.countAllAnime()
    }

    @WorkerThread
    suspend fun countWatchedAnime():Int{
        return animeDAO.countWatchedAnime()
    }

    @WorkerThread
    suspend fun countPlannedAnime():Int{
        return animeDAO.countPlannedAnime()
    }
}