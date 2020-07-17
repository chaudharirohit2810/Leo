package com.rohit2810.leo_kotlin.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.location.Location
//import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.rohit2810.leo_kotlin.models.User
import timber.log.Timber


private const val SHARED_PREFERENCE_PATH = "com.rohit2810.leo.Login"
private const val USERNAME = "username"
private const val PASSWORD = "password"
private const val PHONE = "phone"
private const val NAME = "name"
private const val EMAIL = "email"
private const val PHONE1 = "phone1"
private const val PHONE2 = "phone2"
private const val PHONE3 = "phone3"
private const val PHONE4 = "phone4"
private const val PHONE5 = "phone5"
private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"
private const val IS_INTRO_OPENED = "isIntroOpened"
private const val FALL_DETECTION_SERVICE = "isFallDetectionServiceEnabled"
private const val IS_IN_TROUBLE = "is_in_trouble"
private const val TEMP_INTRO = "temp_intro"


fun addUserToCache(context: Context, user: User) {
    try {
        Timber.d(user.printUtil())
        val sharedPreferencesEditor =
            context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE).edit()
        sharedPreferencesEditor.putString(USERNAME, user.username)
        sharedPreferencesEditor.putString(PASSWORD, user.password)
        sharedPreferencesEditor.putString(PHONE, user.phone)
        sharedPreferencesEditor.putString(NAME, user.name)
        sharedPreferencesEditor.putString(EMAIL, user.email)
        if (!user.emergencyContacts.isNullOrEmpty()) {
            sharedPreferencesEditor.putString(PHONE1, user.emergencyContacts[0])
            sharedPreferencesEditor.putString(PHONE2, user.emergencyContacts[1])
            sharedPreferencesEditor.putString(PHONE3, user.emergencyContacts[2])
            sharedPreferencesEditor.putString(PHONE4, user.emergencyContacts[3])
            sharedPreferencesEditor.putString(PHONE5, user.emergencyContacts[4])
        }
        sharedPreferencesEditor.apply()
        Timber.d("user added successfully")
    } catch (e: Exception) {
        Timber.d(e)
        Timber.d(e.localizedMessage)
    }

}

fun getUserFromCache(context: Context): User? {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)

    sharedPreferences.getString(USERNAME, null)?.let { it ->
        sharedPreferences.getString(PASSWORD, null)?.let { it1 ->
            return User(
                username = it,
                password = it1,
                phone = sharedPreferences.getString(PHONE, null),
                name = sharedPreferences.getString(
                    NAME, null
                ),
                emergencyContacts = getEmergencyContactFromCache(context).toMutableList(),
                email = sharedPreferences.getString(
                    EMAIL, null
                )
            )
        }
    }
    return null
}

fun getEmergencyContactFromCache(context: Context): List<String?> {
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
    val sharedPreferencesEditor =
        context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE).edit()
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


fun saveIntroPrefs(context: Context) {
    val pref: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)
    val editor = pref.edit()
    editor.putBoolean(IS_INTRO_OPENED, true)
    editor.apply()
}

fun getIntroPrefs(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)
    return sharedPreferences.getBoolean(IS_INTRO_OPENED, false)
}

fun getFallDetectionPrefs(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)
    return sharedPreferences.getBoolean(FALL_DETECTION_SERVICE, true)
}

fun saveFallDetectionPrefs(context: Context) {
    val pref: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)
    val editor = pref.edit()
    editor.putBoolean(FALL_DETECTION_SERVICE, !getFallDetectionPrefs(context))
    editor.apply()
}

fun saveIsInTrouble(context: Context, bool: Boolean) {
    val pref: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)
    val editor = pref.edit()
    editor.putBoolean(IS_IN_TROUBLE,bool)
    editor.apply()
}

fun getIsInTrouble(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE)
    return sharedPreferences.getBoolean(IS_IN_TROUBLE, false)
}