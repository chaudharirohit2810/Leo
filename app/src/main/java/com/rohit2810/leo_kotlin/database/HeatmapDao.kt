package com.rohit2810.leo_kotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rohit2810.leo_kotlin.models.map.Heatmap
import com.rohit2810.leo_kotlin.models.map.HeatmapDatabaseModule
import com.rohit2810.leo_kotlin.models.news.News

@Dao
interface HeatmapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeattmap(heatmap: HeatmapDatabaseModule)

    @Query("DELETE FROM heatmap_table")
    suspend fun deleteAllHeatmaps()

    @Query("SELECT * FROM heatmap_table")
    fun getAllHeatmaps() : LiveData<List<HeatmapDatabaseModule>>
}