package com.example.anilibrary.model.data.pagination

import com.example.anilibrary.model.data.pojo.AnimeDetailResponseAPI
import com.example.anilibrary.model.network.ApiConfig

object DetailAnimeSource {
    fun detailSource(id: Int): AnimeDetailResponseAPI? {
        val response = ApiConfig.getApiService().getAnimeDetail(id)
        return response.body()
    }
}