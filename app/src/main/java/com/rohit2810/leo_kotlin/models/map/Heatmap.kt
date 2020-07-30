package com.rohit2810.leo_kotlin.models.map

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Keep
data class Heatmap(
    var region: String,
    var city: String,
    var loc: Location,
    var murder: Int = 0,
    var rape: Int = 0,
    var kidnapping: Int = 0,
    var robbery: Int = 0,
    @Json(name = "holding hostage")
    var holding: Int = 0,
    var riot: Int = 0,
    var arson: Int = 0,
    var assault: Int = 0,
    var covid: Int = 0
) {

}

fun Heatmap.asDatabaseModule(): HeatmapDatabaseModule{
    return HeatmapDatabaseModule(
        count = murder + rape + kidnapping + robbery + holding + riot + arson + assault + covid,
        latitude = loc.coordinates.get(1) ?: 0.0,
        longitude = loc.coordinates.get(0) ?: 0.0
    )
}

@Entity(tableName = "heatmap_table")
data class HeatmapDatabaseModule(
    var count: Int,
    var latitude: Double,
    var longitude: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}