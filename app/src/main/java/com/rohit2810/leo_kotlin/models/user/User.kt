package com.rohit2810.leo_kotlin.models.user

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class User(
    @Json(name = "user_name")
    val username: String,
    var password: String = " ",
    var email: String? = "",
    val name: String? = "",
    var otp: String? = "",
    val phone: String? = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var area: String? = " ",
    var inTrouble: Boolean = false,
    var emergencyContacts : MutableList<String?> = mutableListOf<String?>(),
    var token: String? = ""
) : Parcelable {

    fun printUtil() : String{
        return "Username: ${username} name: ${name} inTrouble: ${inTrouble} phone: ${phone}"
    }

    fun convertToTroubleString() {

    }
}