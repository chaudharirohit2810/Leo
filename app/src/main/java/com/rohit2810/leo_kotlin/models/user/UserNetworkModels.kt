package com.rohit2810.leo_kotlin.models.user

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
class OTP (
    var email: String,
    @Json(name = "user_name")
    var username: String
)

@Keep
data class ValidateOTP(
    @Json(name = "user_name")
    var username: String,
    @Json(name = "email")
    var email: String,
    var otp: Int
)

@Keep
data class Username(
    @Json(name = "user_name")
    var username: String
)

@Keep
data class UnMarkTrouble(
    @Json(name = "user_name")
    var username: String,
    var token: String
)

@Keep
data class EmergencyContactsModel(
    @Json(name = "user_name")
    var username: String,
    var ec1: String? = "",
    var ec2: String? = "",
    var ec3: String? = "",
    var ec4: String? = "",
    var ec5: String? = "",
    var token: String
)