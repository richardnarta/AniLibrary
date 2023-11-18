package com.example.anilibrary.model.data.pojo

import com.google.gson.annotations.SerializedName

data class AnimeSeason(
    @SerializedName("year")
    var year : Int? = null,

    @SerializedName("season")
    var season : String? = null
)
