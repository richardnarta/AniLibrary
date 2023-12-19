package com.example.anilibrary

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

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
        val database: FirebaseDatabase by lazy {
            Firebase.database
        }
        val storage: FirebaseStorage by lazy {
            Firebase.storage
        }
    }
}