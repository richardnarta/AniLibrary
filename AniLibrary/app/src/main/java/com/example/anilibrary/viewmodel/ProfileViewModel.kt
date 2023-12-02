package com.example.anilibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anilibrary.model.data.repository.AnimeListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val animeListRepository: AnimeListRepository): ViewModel() {
    fun countAllAnime(callback: (Int)->Unit){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                animeListRepository.countAllAnime()
            }
            callback(result)
        }
    }

    fun countWatchedAnime(callback: (Int)->Unit){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                animeListRepository.countWatchedAnime()
            }
            callback(result)
        }
    }

    fun countPlannedAnime(callback: (Int)->Unit){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                animeListRepository.countPlannedAnime()
            }
            callback(result)
        }
    }
}