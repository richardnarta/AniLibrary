package com.example.anilibrary.model.data.pojo

import com.google.gson.annotations.SerializedName

data class AnimeAlternateTitle(
    @SerializedName("synonyms")
    var synonym : ArrayList<String>? = arrayListOf(),

    @SerializedName("en")
    var en : String? = null,

    @SerializedName("ja")
    var ja : String? = null
)
