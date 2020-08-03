package com.rohit2810.leo_kotlin.models.news

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unsafe_news_table")
data class UnsafeNews(
    var Latitude: Double,
    var Longitude: Double,
    var headline: String = " ",
    var url: String = " "
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

data class UnsafeNewsNetwork(
    var Latitude: Double,
    var Longitude: Double,
    var News: List<BasicNews>
)

data class BasicNews(
        var Headline: String,
        var Url: String
)

fun UnsafeNewsNetwork.asDatabaseModule(): UnsafeNews {
    var list = News
    var head = " "
    var url = " "
    if (!list.isNullOrEmpty()) {
        head = list[0].Headline
        url = list[0].Url
    }
    return UnsafeNews(Latitude, Longitude, headline = head, url = url)
}