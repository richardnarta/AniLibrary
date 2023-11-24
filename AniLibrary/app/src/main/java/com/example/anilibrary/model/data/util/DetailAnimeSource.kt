package com.example.anilibrary.model.data.util

import com.example.anilibrary.model.data.pojo.AnimeDetailResponseAPI
import com.example.anilibrary.model.network.ApiConfig

object DetailAnimeSource {
    suspend fun detailSource(id: Int): AnimeDetailResponseAPI? {
        val response = ApiConfig.getApiService().getAnimeDetail(id)
        return response.body()
    }
}