package com.example.leo_kotlin.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    @Json(name = "user_name")
    val username: String,
    val password: String,
    val name: String? = "",
    val phone: String? = "",
    var longitude: Double? = 0.0,
    var latitude: Double? = 0.0,
    var area: String? = "",
    var inTrouble: Boolean = false,
    var emergencyContacts : MutableList<String?> = mutableListOf<String?>()
) : Parcelable {

    fun printUtil() : String{
        return "Username: ${username} name: ${name} inTrouble: ${inTrouble} phone: ${phone}"
    }
}