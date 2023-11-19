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

    val default= arrayOf("fall", "2023")
    private val currentSeason = MutableLiveData(default)
    val animeFlow = currentSeason.switchMap {
        seasonAnimeRepository.getAnime(it[0], it[1]).cachedIn(viewModelScope)
    }

    fun setSeason(season: Array<String>){
        currentSeason.value = season
    }
}

class ViewModelFactory(private val context: HomeFragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(Injection.provideAnimeSeasonRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}