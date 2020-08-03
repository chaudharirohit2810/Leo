package com.rohit2810.leo_kotlin.database

import android.content.Context
import androidx.room.*
import com.rohit2810.leo_kotlin.models.map.Heatmap
import com.rohit2810.leo_kotlin.models.map.HeatmapDatabaseModule
import com.rohit2810.leo_kotlin.models.map.RouteDataBaseModel
import com.rohit2810.leo_kotlin.models.news.News
import com.rohit2810.leo_kotlin.models.news.NewsDatabaseModule
import com.rohit2810.leo_kotlin.models.news.UnsafeNews

@Database(entities = arrayOf(NewsDatabaseModule::class, HeatmapDatabaseModule::class, RouteDataBaseModel::class, UnsafeNews::class), version = 1, exportSchema = false)
abstract class LeoDatabase : RoomDatabase() {
    abstract val newsDao: NewsDao
    abstract val heatmapDao: HeatmapDao
}

private lateinit var INSTANCE: LeoDatabase

fun getDatabase(context: Context): LeoDatabase {
    synchronized(LeoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                LeoDatabase::class.java,
                "maindatabase"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}