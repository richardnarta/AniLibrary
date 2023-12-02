package com.example.anilibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.anilibrary.di.Injection
import com.example.anilibrary.model.data.repository.AnimeListRepository

class ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(Injection.provideAnimeSeasonRepository()) as T
        }else if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExploreViewModel(Injection.provideAnimeSearchRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class DBViewModelFactory(private val repository: AnimeListRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(ListViewModel::class.java)){
            return ListViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}