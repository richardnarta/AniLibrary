package com.example.anilibrary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.anilibrary.di.Injection
import com.example.anilibrary.model.data.repository.SeasonAnimeRepository
import com.example.anilibrary.ui.fragment.HomeFragment

class HomeViewModel(seasonAnimeRepository: SeasonAnimeRepository): ViewModel() {

    private val default= arrayOf("fall", "2023")
    val currentSeason = MutableLiveData(default)
    val animeFlow = currentSeason.switchMap {
        seasonAnimeRepository.getAnime(it[0].lowercase(), it[1]).cachedIn(viewModelScope)
    }

    fun setSeason(season: Array<String>){
        currentSeason.value = season
    }
}