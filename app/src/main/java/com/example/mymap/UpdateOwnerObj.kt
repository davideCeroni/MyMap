package com.example.mymap

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOwnerObj(var fightpoint_uuid: String, var score: Int)