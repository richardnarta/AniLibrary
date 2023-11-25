package com.example.anilibrary.model.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.anilibrary.model.data.util.Constants

@Entity(tableName = Constants.ANIME_TABLE)
data class AnimeListEntity(
    @PrimaryKey(autoGenerate = false) var id:Int? = null,

    @ColumnInfo(name = "list_type") val listType: String,
    @ColumnInfo(name = "anime_poster") val animePoster: String,
    @ColumnInfo(name = "anime_title") val animeTitle: String,
    @ColumnInfo(name = "anime_season") val animeSeason: String,
    @ColumnInfo(name = "anime_year") val animeYear: Int,
    @ColumnInfo(name = "anime_rating") val animeRating: Float
)
