package com.example.leo_kotlin.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.location.Location
import com.example.leo_kotlin.models.User
import timber.log.Timber
import java.lang.Exception

private val SHARED_PREFERENCE_PATH = "com.example.leo.Login"
private val USERNAME = "username"
private val PASSWORD = "password"
private val PHONE = "phone"
private val NAME = "name"
private val PHONE1 = "phone1"
private val PHONE2 = "phone2"
private val PHONE3 = "phone3"
private val PHONE4 = "phone4"
private val PHONE5 = "phone5"
private val LATITUDE = "latitude"
private val LONGITUDE = "longitude"


fun addUserToCache(context: Context, user: User) {
    try {
        Timber.d(user.printUtil())
//        Timber.d(user.emergencyContacts)
        val sharedPreferencesEditor = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE).edit()
        sharedPreferencesEditor.putString(USERNAME, user.username)
        sharedPreferencesEditor.putString(PASSWORD, user.password)
        sharedPreferencesEditor.putString(PHONE, user.phone)
        sharedPreferencesEditor.putString(NAME, user.name)
        if(!user.emergencyContacts.isNullOrEmpty()) {
            sharedPreferencesEditor.putString(PHONE1, user.emergencyContacts[0])
            sharedPreferencesEditor.putString(PHONE2, user.emergencyContacts[1])
            sharedPreferencesEditor.putString(PHONE3, user.emergencyContacts[2])
            sharedPreferencesEditor.putString(PHONE4, user.emergencyContacts[3])
            sharedPreferencesEditor.putString(PHONE5, user.emergencyContacts[4])
        }
        sharedPreferencesEditor.apply()
        Timber.d("user added successfully")
    }catch (e: Exception) {
        Timber.d(e)
        Timber.d(e.localizedMessage)
    }

}

fun getUserFromCache(context: Context): User?{
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)

    sharedPreferences.getString(USERNAME, null)?.let {it ->
        sharedPreferences.getString(PASSWORD, null)?.let { it1 ->
            return User(username = it, password = it1, phone = sharedPreferences.getString(PHONE, null), name = sharedPreferences.getString(
                NAME, null))
        }
    }
    return null
}

fun getEmergencyContactFromCache(context: Context): List<String?>{
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)

    var phone1 = sharedPreferences.getString(PHONE1, "")
    var phone2 = sharedPreferences.getString(PHONE2, "")
    var phone3 = sharedPreferences.getString(PHONE3, "")
    var phone4 = sharedPreferences.getString(PHONE4, "")
    var phone5 = sharedPreferences.getString(PHONE5, "")

    var list = listOf<String?>(phone1, phone2, phone3, phone4, phone5)
    return list

}

fun deleteUserFromCache(context: Context) {
    context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE).edit().clear().apply()
}

fun addLocationinCache(context: Context, location: Location) {
    val sharedPreferencesEditor = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE).edit()
    sharedPreferencesEditor.putFloat(LATITUDE, location.latitude.toFloat())
    sharedPreferencesEditor.putFloat(LONGITUDE, location.longitude.toFloat())
    sharedPreferencesEditor.apply()
}

fun getLatitudeFromCache(context: Context): Float {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)
    return sharedPreferences.getFloat(LATITUDE, 0.0F)
}

fun getLongitudeFromCache(context: Context): Float {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)
    return sharedPreferences.getFloat(LONGITUDE, 0.0F)
}

fun addPermissionPreference(context: Context) {
    val sharedPreferencesEditor = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE).edit()
    sharedPreferencesEditor.putBoolean("PERMISSIONS", true)
    sharedPreferencesEditor.apply()
}

fun removePermissionPreference(context: Context) {
    val sharedPreferencesEditor = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE).edit()
    sharedPreferencesEditor.putBoolean("PERMISSIONS", false)
    sharedPreferencesEditor.apply()
}

fun getPermissionPreference(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)
    return sharedPreferences.getBoolean("PERMISSIONS", false)
}
