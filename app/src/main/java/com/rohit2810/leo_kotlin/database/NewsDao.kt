package com.rohit2810.leo_kotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rohit2810.leo_kotlin.models.news.News
import com.rohit2810.leo_kotlin.models.news.NewsDatabaseModule

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsDatabaseModule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNews(news: List<NewsDatabaseModule>)

    @Query("DELETE FROM news_table")
    suspend fun deleteAllNews()

    @Query("SELECT * FROM news_table")
    fun getAllNews() : LiveData<List<NewsDatabaseModule>>
}