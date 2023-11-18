package com.example.anilibrary.model.data.pojo

import com.google.gson.annotations.SerializedName

data class AnimeMainPicture(
    @SerializedName("medium")
    var medium : String? = null,

    @SerializedName("large")
    var large  : String? = null
)
