package com.rohit2810.leo_kotlin.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rohit2810.leo_kotlin.NetworkUtils.NetworkConnectionInterceptor
import com.rohit2810.leo_kotlin.models.map.Heatmap
import com.rohit2810.leo_kotlin.models.news.News
import com.rohit2810.leo_kotlin.models.user.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://leomine-backend.herokuapp.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object MineApi {
    fun retrofitService(context: Context): MineServiceApi {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(NetworkConnectionInterceptor(context))
        val retrofit = Retrofit.Builder()
            .client(okHttpClient.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()
        return retrofit.create(MineServiceApi::class.java)
    }
}


interface MineServiceApi{
    // To get news
    @GET("/news/{city}")
    suspend fun getNews(@Path("city") city: String): List<News>

    @GET("/crimes")
    suspend fun getHeatmaps(@Query("latitude") latitude: Double,
    @Query("longitude") longitude: Double): List<Heatmap>

}