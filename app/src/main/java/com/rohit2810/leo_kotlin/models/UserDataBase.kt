package com.rohit2810.leo_kotlin.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "user_database")
class UserDatabaseModule(
    @PrimaryKey
    val id : Int = 1,
    @Json(name = "user_name")
    val username: String,
    val password: String,
    val name: String? = "",
    val phone: String? = "",
    var longitude: Double? = 0.0,
    var latitude: Double? = 0.0,
    var area: String? = "",
    var inTrouble: Boolean = false,
    var phone1: String?,
    val phone2: String?,
    val phone3: String?,
    val phone4: String?,
    val phone5: String?
) {

}

fun User.asDatabaseModule() : UserDatabaseModule {
    return UserDatabaseModule(
        username = username,
        password = password,
        name = name,
        phone = phone,
        longitude = longitude,
        latitude = latitude,
        area = area,
        inTrouble = inTrouble,
        phone1 = emergencyContacts.get(0),
        phone2 = emergencyContacts.get(1),
        phone3 = emergencyContacts.get(2),
        phone4 = emergencyContacts.get(3),
        phone5 = emergencyContacts.get(4)
    )
}

fun UserDatabaseModule.asMainModule(): User{
    return User(
        username = username,
        password = password,
        name = name,
        phone = phone,
        longitude = longitude,
        latitude = latitude,
        area = area,
        inTrouble = inTrouble,
        emergencyContacts = mutableListOf(phone1, phone2, phone3, phone4, phone5)
    )
}