package com.rohit2810.leo_kotlin.models.news


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.sql.Date
import java.util.*

data class News(
    var headline: String,
    var url: String = " ",
    var crime: String = " ",
    var location: String = " ",
    var region: String = " ",
    var city: String = " "
//    var date: MyDate?
) {
}

data class MyDate(
    @Json(name = "\$date")
    var date: Long = 0
)

@Entity(tableName = "news_table")
data class NewsDatabaseModule(
    var headline: String,
    var url: String = " ",
    var crime: String = " ",
    var location: String = " ",
    var region: String = " ",
    var city: String = " ",
    var date: Long = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun News.asDatabaseModule(): NewsDatabaseModule {
    return NewsDatabaseModule(
        headline, url, crime, location, region, city
    )
}