package com.example.anilibrary

import android.app.Application
import android.content.Context

class AniLibrary : Application() {
    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        var instance: AniLibrary? = null
            private set
        val context: Context?
            get() = instance
    }
}