package com.example.anilibrary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.anilibrary.model.data.database.AnimeListEntity
import com.example.anilibrary.model.data.repository.AnimeListRepository
import kotlinx.coroutines.launch

class ListViewModel(private val animeListRepository: AnimeListRepository): ViewModel() {
    val allAnimeList: LiveData<List<AnimeListEntity>> = animeListRepository.allAnime.asLiveData()
    val allWatchedList: LiveData<List<AnimeListEntity>> = animeListRepository.watchedAnime.asLiveData()
    val allPlannedList: LiveData<List<AnimeListEntity>> = animeListRepository.plannedAnime.asLiveData()

    fun deleteAnime(id : Int) = viewModelScope.launch {
        animeListRepository.deleteAnimeFromId(id)
    }
}