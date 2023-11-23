package com.example.anilibrary.model.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.anilibrary.model.data.pagination.SearchAnimePagingSource
import com.example.anilibrary.model.data.pojo.AnimeNode

class SearchAnimeRepository {
    fun getAnime(query:String): LiveData<PagingData<AnimeNode>> {
        return Pager(
            PagingConfig(pageSize = 1)
        ) {
            SearchAnimePagingSource(query)
        }.liveData
    }
}