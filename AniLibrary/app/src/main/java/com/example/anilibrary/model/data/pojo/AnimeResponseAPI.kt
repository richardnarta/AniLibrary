package com.example.anilibrary.model.data.pojo

import com.google.gson.annotations.SerializedName

data class AnimeResponseAPI(
    @SerializedName("data")
    var data : ArrayList<AnimeData> = arrayListOf()
)
