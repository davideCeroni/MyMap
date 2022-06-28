package com.example.mymap

import kotlinx.serialization.Serializable

@Serializable
data class User(var firebase_id: String, var username: String, var avatar: Int)