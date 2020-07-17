package com.rohit2810.leo_kotlin.models

import com.squareup.moshi.Json

class Trouble(
    @Json(name = "user_name")
    val username: String,
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val inTrouble: Boolean,
    var emergencyContacts : MutableList<String?> = mutableListOf<String?>()
)