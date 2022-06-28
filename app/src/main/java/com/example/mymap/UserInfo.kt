package com.example.mymap

data class UserInfo(var firebase_id: String, var username: String, var avatar: Int, var createdAt: String, var fightpointsOwned: ArrayList<FightPoint>, var notifications: ArrayList<Notification>)