package com.rohit2810.leo_kotlin.models.news


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.sql.Date
import java.util.*

@Entity(tableName = "news_table")
data class News(
    var headline: String,
    var url: String = " ",
    var crime: String = " ",
    var location: String = " ",
    var region: String = " ",
    var city: String = " "
//    var date: String = " "
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}