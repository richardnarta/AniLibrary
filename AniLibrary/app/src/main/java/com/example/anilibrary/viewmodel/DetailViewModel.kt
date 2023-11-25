package com.example.anilibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anilibrary.model.data.database.AnimeListEntity
import com.example.anilibrary.model.data.pojo.AnimeDetailResponseAPI
import com.example.anilibrary.model.data.repository.AnimeListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(private val animeListRepository: AnimeListRepository): ViewModel() {
    val dataAnime = MutableLiveData<AnimeDetailResponseAPI>()

    fun insertAnime(anime: AnimeListEntity) = viewModelScope.launch {
        animeListRepository.insertAnime(anime)
    }

    fun checkAnime(id: Int, callback: (Int)->Unit){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                animeListRepository.isAnimeAdded(id)
            }
            callback(result)
        }
    }
}