package com.rohit2810.leo_kotlin.models.map

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Route(
    var directions: List<List<Double>>
)
@Entity(tableName = "directions_table")
data class RouteDataBaseModel(
    var latitude: Double,
    var longitude: Double
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun Route.asDatabaseModule(): List<RouteDataBaseModel> {
    var list = mutableListOf<RouteDataBaseModel>()
    for (direction in directions) {
        var route = RouteDataBaseModel(direction[1], direction[0])
        list.add(route)
    }
    return list
}