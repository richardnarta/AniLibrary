package com.example.anilibrary.model.network

import com.example.anilibrary.model.data.pojo.AnimeDetailResponseAPI
import com.example.anilibrary.model.data.pojo.AnimeResponseAPI
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("X-MAL-CLIENT-ID: a194edc733fff7b6085829f0ca5cb8a5")
    @GET("season/{year}/{season}")
    suspend fun getSeasonAnime(
        @Path("year") year:String,
        @Path("season") season: String,
        @Query("offset") offset: Int,
        @Query("sort") sort:String = "anime_num_list_users",
        @Query("fields") fields:String = "id,title,main_picture,start_season,mean,media_type,num_episodes"
    ): Response<AnimeResponseAPI>

    @Headers("X-MAL-CLIENT-ID: a194edc733fff7b6085829f0ca5cb8a5")
    @GET("")
    suspend fun getSearchAnime(
        @Query("q") q:String,
        @Query("offset") offset: Int,
        @Query("fields") fields:String = "id,title,main_picture,start_season,mean"
    ): Response<AnimeResponseAPI>

    @Headers("X-MAL-CLIENT-ID: a194edc733fff7b6085829f0ca5cb8a5")
    @GET("{id}")
    suspend fun getAnimeDetail(
        @Path("id") id:Int,
        @Query("fields") fields:String = "id,title,main_picture,alternative_titles,synopsis,mean,num_scoring_users,media_type,status,genres,num_episodes,start_season,studios"
    ): Response<AnimeDetailResponseAPI>
}