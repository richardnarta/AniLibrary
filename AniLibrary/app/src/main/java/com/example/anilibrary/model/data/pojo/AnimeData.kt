package com.example.anilibrary.model.data.pojo

import com.google.gson.annotations.SerializedName

data class AnimeData(
    @SerializedName("node")
    var node : AnimeNode = AnimeNode()
)
