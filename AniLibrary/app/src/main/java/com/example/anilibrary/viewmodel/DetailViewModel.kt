package com.example.anilibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anilibrary.model.data.pojo.AnimeDetailResponseAPI

class DetailViewModel: ViewModel() {
    val dataAnime = MutableLiveData<AnimeDetailResponseAPI>()
}