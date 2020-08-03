package com.rohit2810.leo_kotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rohit2810.leo_kotlin.models.map.Heatmap
import com.rohit2810.leo_kotlin.models.map.HeatmapDatabaseModule
import com.rohit2810.leo_kotlin.models.map.Route
import com.rohit2810.leo_kotlin.models.map.RouteDataBaseModel
import com.rohit2810.leo_kotlin.models.news.News
import com.rohit2810.leo_kotlin.models.news.UnsafeNews

@Dao
interface HeatmapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeattmap(heatmap: HeatmapDatabaseModule)

    @Query("DELETE FROM heatmap_table")
    suspend fun deleteAllHeatmaps()

    @Query("SELECT * FROM heatmap_table")
    fun getAllHeatmaps() : LiveData<List<HeatmapDatabaseModule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirections(routeDataBaseModel: RouteDataBaseModel)

    @Query("SELECT * FROM directions_table")
    fun getDirections(): LiveData<List<RouteDataBaseModel>>

    @Query("DELETE FROM directions_table")
    suspend fun deleteAllDirections()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnsafe(unsafe: UnsafeNews)

    @Query("SELECT * FROM unsafe_news_table")
    fun getUnsafe(): LiveData<List<UnsafeNews>>

    @Query("DELETE FROM unsafe_news_table")
    suspend fun deleteAllUnsafe()

    @Query("SELECT * from unsafe_news_table where id =:id ")
    fun getSingleUnsafe(id: Int): LiveData<UnsafeNews>
}