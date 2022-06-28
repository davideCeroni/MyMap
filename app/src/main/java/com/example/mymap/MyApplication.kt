package com.example.mymap

import android.app.Application
import com.google.firebase.auth.FirebaseUser

class MyApplication: Application() {
    var token: String? = null
    var currentFirebaseUser: FirebaseUser? = null
    override fun onCreate() {
        instance = this
        super.onCreate()
    }
    companion object{
        lateinit var instance: MyApplication
    }
}