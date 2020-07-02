package com.example.leo_kotlin.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.leo_kotlin.models.User
import com.example.leo_kotlin.network.UserApi
import com.example.leo_kotlin.utils.*
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

// Utility function to do all regarding user means add new user, login and logout user and mark trouble
class TroubleRepository private constructor(private val context: Context) {
    private val service = UserApi.retrofitService


    suspend fun loginUser(user: User) {
        try {
            val deferredUser = service.loginUser(user)
            val user2 = deferredUser.await()
            Timber.d(user2.printUtil())
            addUserToCache(context, user2)
        } catch (e: Exception) {
            Timber.d(e.cause)
            Timber.d(e.localizedMessage)
            throw Exception("Invalid username or password")
        }
    }

    suspend fun registerUser(user: User) {
            try {
                Timber.d(user.printUtil())
                val deferredUser = service.createUser(user)
                val user2 = deferredUser.await()
                addUserToCache(context, user2)
            } catch (e: Exception) {
                throw Exception(e.localizedMessage)
            }
    }

    suspend fun logoutUser() {
        withContext(Dispatchers.IO) {
            try {
                //Delete Shared Preference
                deleteUserFromCache(context)
            } catch (e: Exception) {
                throw Exception(e.localizedMessage)
            }
        }
    }

    suspend fun updateUser(user: User) {
        try {
            val deferredUser = UserApi.retrofitService.createTrouble(user)
            val user2 = deferredUser.await()
            Timber.d(user2.emergencyContacts.toString())
            addUserToCache(context, user)
        } catch (e: Exception) {
            throw Exception(e.localizedMessage)
        }
    }

    suspend fun markTrouble(user: User) {
        try {
            var latitude = getLatitudeFromCache(context).toDouble()
            var longitude = getLongitudeFromCache(context).toDouble()
            user.inTrouble = true
            user.latitude = latitude
            user.longitude = longitude
            playAudio()
            Timber.d("Latitude: ${latitude} Longitude: ${longitude}")
            // Skip this for now
            // Later get data using shared preference
            sendMessage(latitude, longitude, getEmergencyContactFromCache(context))
            val deferredUser = UserApi.retrofitService.createTrouble(user)
            val user2 = deferredUser.await()
        } catch (e: Exception) {
            throw Exception(e.localizedMessage)
        }
    }

    suspend fun checkLogin(user: User) {
        try {
            val deferredUser = service.loginUser(user)
            val user2 = deferredUser.await()
            Timber.d(user2.printUtil())
        } catch (e: Exception) {
            throw Exception(e.localizedMessage)
        }
    }

    companion object {
        private lateinit var INSTANCE: TroubleRepository

        fun getInstance(context: Context): TroubleRepository {
            synchronized(TroubleRepository::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = TroubleRepository(context)
                }
            }
            return INSTANCE
        }
    }


}

