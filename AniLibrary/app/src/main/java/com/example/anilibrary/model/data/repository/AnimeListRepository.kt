package com.example.anilibrary.model.data.repository

import androidx.annotation.WorkerThread
import com.example.anilibrary.model.data.database.AnimeListDAO
import com.example.anilibrary.model.data.database.AnimeListEntity
import kotlinx.coroutines.flow.Flow

class AnimeListRepository(private val animeDAO: AnimeListDAO) {
    val allAnime: Flow<List<AnimeListEntity>> = animeDAO.getAllAnime()
    val watchedAnime: Flow<List<AnimeListEntity>> = animeDAO.getWatchedAnime()
    val plannedAnime: Flow<List<AnimeListEntity>> = animeDAO.getPlannedAnime()

    @WorkerThread
    suspend fun insertAnime(animeData: AnimeListEntity){
        animeDAO.insertAnime(animeData)
    }

    @WorkerThread
    suspend fun deleteAnimeFromId(id: Int){
        animeDAO.deleteAnimeFromId(id)
    }
}