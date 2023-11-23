package com.example.anilibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.anilibrary.di.Injection
import com.example.anilibrary.model.data.repository.SearchAnimeRepository

class ExploreViewModel(animeRepository: SearchAnimeRepository) : ViewModel() {

    private val currentQuery = MutableLiveData("")
    val animeFlow = currentQuery.switchMap {
        animeRepository.getAnime(it).cachedIn(viewModelScope)
    }

    fun searchAnime(query: String){
        currentQuery.value = query
    }

    fun getCurrentQuery():String?{
        return currentQuery.value
    }
}