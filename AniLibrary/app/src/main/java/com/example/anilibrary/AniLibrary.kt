package com.example.anilibrary

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
        val auth: FirebaseAuth by lazy {
            Firebase.auth
        }
    }
}