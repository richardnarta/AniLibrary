package com.example.anilibrary.model.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.anilibrary.model.data.pagination.SeasonAnimePagingSource
import com.example.anilibrary.model.data.pojo.AnimeNode

class SeasonAnimeRepository {
    fun getAnime(season:String, year:String): LiveData<PagingData<AnimeNode>> {
        return Pager(
            PagingConfig(pageSize = 1)
        ) {
            SeasonAnimePagingSource(season, year)
        }.liveData
    }
}