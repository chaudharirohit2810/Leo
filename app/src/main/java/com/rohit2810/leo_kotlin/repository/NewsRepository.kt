package com.rohit2810.leo_kotlin.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import com.rohit2810.leo_kotlin.database.getDatabase
import com.rohit2810.leo_kotlin.models.news.News
import com.rohit2810.leo_kotlin.models.news.asDatabaseModule
import com.rohit2810.leo_kotlin.network.MineApi
import com.rohit2810.leo_kotlin.network.UserApi
import com.rohit2810.leo_kotlin.utils.getLatitudeFromCache
import com.rohit2810.leo_kotlin.utils.getLongitudeFromCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import java.util.*

class NewsRepository private constructor(var context: Context) {
    private val service = MineApi.retrofitService(context)
    private val database = getDatabase(context).newsDao
    var city = "pune"
    suspend fun insertNews() {
        withContext(Dispatchers.IO) {
            try {
                val gcd = Geocoder(context, Locale.getDefault())
                val addresses: List<Address> =
                    gcd.getFromLocation(
                        getLatitudeFromCache(context).toDouble(),
                        getLongitudeFromCache(context).toDouble(),
                        1
                    )
                if (addresses.isNotEmpty()) {
                    city = addresses[0].getLocality()
                }

                var def = service.getNews(city)
                Log.d("NewsRepository", def.toString())
                Timber.d(def.toString())
                if(def.isEmpty()) {
                     def = service.getNews("pune")
                }
                Log.d("NewsRepository", def.toString())
                database.deleteAllNews()
                def.map { it ->
                    Log.d("NewsRepository", it.toString())
                    database.insertNews(it.asDatabaseModule())
                }
            }catch (e: Exception) {
                Log.d("NewsRepository", e.localizedMessage)
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