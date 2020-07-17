package com.rohit2810.leo_kotlin.models


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "news_article_table")
@SuppressLint("ParcelCreator")
@Parcelize
data class Article(
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,
    val title: String?,
    val url: String? = null,
    val urlToImage: String? = null
): Parcelable {

    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0

    fun setId(id: Int) {
        this.id = id
    }

    fun getId() : Int {
        return id
    }
}