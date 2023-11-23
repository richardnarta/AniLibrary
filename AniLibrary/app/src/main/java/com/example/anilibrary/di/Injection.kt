package com.example.anilibrary.di

import com.example.anilibrary.model.data.repository.SearchAnimeRepository
import com.example.anilibrary.model.data.repository.SeasonAnimeRepository

object Injection {
    fun provideAnimeSeasonRepository(): SeasonAnimeRepository {
        return SeasonAnimeRepository()
    }

    fun provideAnimeSearchRepository(): SearchAnimeRepository {
        return SearchAnimeRepository()
    }
}