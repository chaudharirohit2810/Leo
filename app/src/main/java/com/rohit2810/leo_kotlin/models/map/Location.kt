package com.rohit2810.leo_kotlin.models.map

import androidx.annotation.Keep

@Keep
data class Location(
    var type: String,
    var coordinates: List<Double>
) {

}