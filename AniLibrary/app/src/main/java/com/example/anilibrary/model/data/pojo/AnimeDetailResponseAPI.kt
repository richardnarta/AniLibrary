package com.example.anilibrary.model.data.pojo

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponseAPI(
    @SerializedName("id")
    var id : Int? = null,

    @SerializedName("title")
    var title : String? = null,

    @SerializedName("main_picture")
    var mainPicture : AnimeMainPicture? = AnimeMainPicture(),

    @SerializedName("alternative_titles")
    var altTitle : AnimeAlternateTitle? = AnimeAlternateTitle(),

    @SerializedName("synopsis")
    var synopsis : String? = null,

    @SerializedName("mean")
    var rating : Float? = null,

    @SerializedName("rank")
    var rank : Int? = null,

    @SerializedName("num_scoring_users")
    var numUsers : Int? = null,

    @SerializedName("media_type")
    var mediaType : String? = null,

    @SerializedName("status")
    var status : String? = null,

    @SerializedName("source")
    var source : String? = null,

    @SerializedName("genres")
    var genre : ArrayList<AnimeGenre>? = arrayListOf(),

    @SerializedName("num_episodes")
    var numEpisodes : Int? = null,

    @SerializedName("start_season")
    var startSeason : AnimeSeason? = AnimeSeason(),

    @SerializedName("studios")
    var studio : ArrayList<AnimeStudio>? = arrayListOf()
)
