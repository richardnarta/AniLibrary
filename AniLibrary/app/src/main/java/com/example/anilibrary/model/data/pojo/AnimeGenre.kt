package com.example.anilibrary.model.data.pojo

import com.google.gson.annotations.SerializedName

data class AnimeGenre(
    @SerializedName("id")
    var id : Int? = null,

    @SerializedName("name")
    var name : String? = null
)
