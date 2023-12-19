package com.example.anilibrary.model.data.pojo

import com.google.gson.annotations.SerializedName

data class AnimeResponseAPI(
    @SerializedName("data")
    var data : ArrayList<AnimeData> = arrayListOf()
)

data class AnimeData(
    @SerializedName("node")
    var node : AnimeNode = AnimeNode()
)

data class AnimeNode(
    @SerializedName("id")
    var id : Int? = null,

    @SerializedName("title")
    var title : String? = null,

    @SerializedName("main_picture")
    var mainPicture : AnimeMainPicture? = AnimeMainPicture(),

    @SerializedName("start_season")
    var startSeason : AnimeSeason? = AnimeSeason(),

    @SerializedName("mean")
    var rank : Float? = null,

    @SerializedName("media_type")
    var mediaType : String? = null,

    @SerializedName("num_episodes")
    var numEpisodes : Int? = null,
)
