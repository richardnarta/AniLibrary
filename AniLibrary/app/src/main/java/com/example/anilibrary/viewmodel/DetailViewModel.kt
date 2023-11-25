package com.example.anilibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anilibrary.model.data.database.AnimeListEntity
import com.example.anilibrary.model.data.pojo.AnimeDetailResponseAPI
import com.example.anilibrary.model.data.repository.AnimeListRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val animeListRepository: AnimeListRepository): ViewModel() {
    val dataAnime = MutableLiveData<AnimeDetailResponseAPI>()

    fun insertAnime(anime: AnimeListEntity) = viewModelScope.launch {
        animeListRepository.insertAnime(anime)
    }
}