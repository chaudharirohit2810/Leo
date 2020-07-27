package com.rohit2810.leo_kotlin.repository

import android.content.Context
import com.rohit2810.leo_kotlin.database.getDatabase
import com.rohit2810.leo_kotlin.models.news.News
import com.rohit2810.leo_kotlin.network.MineApi
import com.rohit2810.leo_kotlin.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class NewsRepository private constructor(var context: Context) {
    private val service = MineApi.retrofitService(context)
    private val database = getDatabase(context)
    suspend fun insertNews() {
        withContext(Dispatchers.IO) {
            try {
                var def = service.getNews("pune")
//                Timber.d(def.toString())
                database.newsDao.deleteAllNews()
                database.newsDao.insertAllNews(def)
            }catch (e: Exception) {
                Timber.d(e.localizedMessage)
            }

        }
    }


    companion object {
        private lateinit var INSTANCE: NewsRepository

        fun getInstance(context: Context): NewsRepository {
            synchronized(NewsRepository::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = NewsRepository(context)
                }
            }
            return INSTANCE
        }
    }
}