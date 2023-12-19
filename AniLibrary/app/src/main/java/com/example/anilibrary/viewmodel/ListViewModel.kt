package com.example.anilibrary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.anilibrary.model.data.database.AnimeListEntity
import com.example.anilibrary.model.data.repository.AnimeListRepository
import kotlinx.coroutines.launch

class ListViewModel(private val animeListRepository: AnimeListRepository): ViewModel() {
    fun showAllAnimeByQuery(query: String,callback: (LiveData<List<AnimeListEntity>>)->Unit){
        val allAnimeList: LiveData<List<AnimeListEntity>> = animeListRepository.getAllAnimeByQuery(query).asLiveData()
        callback(allAnimeList)
    }

    fun showWatchedAnimeByQuery(query: String,callback: (LiveData<List<AnimeListEntity>>)->Unit){
        val allAnimeList: LiveData<List<AnimeListEntity>> = animeListRepository.getWatchedAnimeByQuery(query).asLiveData()
        callback(allAnimeList)
    }

    fun showPlannedAnimeByQuery(query: String,callback: (LiveData<List<AnimeListEntity>>)->Unit){
        val allAnimeList: LiveData<List<AnimeListEntity>> = animeListRepository.getPlannedAnimeByQuery(query).asLiveData()
        callback(allAnimeList)
    }

    fun deleteAnime(id : Int) = viewModelScope.launch {
        animeListRepository.deleteAnimeFromId(id)
    }
}