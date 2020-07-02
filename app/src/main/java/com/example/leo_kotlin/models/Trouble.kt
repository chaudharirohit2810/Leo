package com.example.leo_kotlin.models

import com.squareup.moshi.Json

class Trouble(
    @Json(name = "user_name")
    val username: String,
    val latitude: Double,
    val longitude: Double,
    val inTrouble: Boolean,
    val area: String,
    val emergencyContacts: List<String?>?

) {
}