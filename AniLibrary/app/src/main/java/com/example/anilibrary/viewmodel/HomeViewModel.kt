package com.example.anilibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.anilibrary.model.data.repository.SeasonAnimeRepository

class HomeViewModel(seasonAnimeRepository: SeasonAnimeRepository): ViewModel() {

    val default= arrayOf("Winter", "2024")
    private val defaultYear = arrayOf(2024, 2023)
    private val defaultSeason = arrayOf("Winter", "Fall", "Summer", "Spring")
    val currentSeason = MutableLiveData(default)
    var season = MutableLiveData(defaultSeason)
    val year = MutableLiveData(defaultYear)

    val animeFlow = currentSeason.switchMap {
        seasonAnimeRepository.getAnime(it[0].lowercase(), it[1]).cachedIn(viewModelScope)
    }

    fun setSeason(season: Array<String>){
        currentSeason.value = season
    }
}