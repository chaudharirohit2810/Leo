package com.rohit2810.leo_kotlin.repository

import android.content.Context
import com.rohit2810.leo_kotlin.NetworkUtils.NoConnectivityException
import com.rohit2810.leo_kotlin.models.*
import com.rohit2810.leo_kotlin.network.UserApi
import com.rohit2810.leo_kotlin.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

// Utility function to do all regarding user means add new user, login and logout user and mark trouble
class TroubleRepository private constructor(private val context: Context) {
    private val service = UserApi.retrofitService(context)


    suspend fun loginUser(user: User) {
        try {
            var pass = user.password
            user.password = sha256(pass)
            val deferredUser = service.loginUser(user)
            val user2 = deferredUser.await()
            user2.password = pass
            Timber.d(user2.toString())
            addUserToCache(context, user2)
            saveIsInTrouble(context, user2.inTrouble)
        } catch (e: NoConnectivityException) {
            throw NoConnectivityException()
        } catch (e: Exception) {
            Timber.d(e.localizedMessage)
            throw Exception("Invalid username or password")
        }
    }

    suspend fun registerUser(user: User) {
        try {
            Timber.d(user.toString())
            var pass = user.password
            user.password = sha256(pass)
            val deferredUser = service.createUser(user)
            val user2 = deferredUser.await()
            user2.password = pass
            addUserToCache(context, user2)
        } catch (e: NoConnectivityException) {
            throw Exception("No Internet Connection")
        } catch (e: Exception) {
            Timber.d(e)
            throw Exception("Something went wrong")
        }
    }

    suspend fun checkUsername(user: User) {
        try {
            var def = service.validateUserName(Username(user.username))
            Timber.d(def.await().toString())
        }catch (e: NoConnectivityException) {
            throw NoConnectivityException()
        }catch (e: Exception) {
            throw Exception("Username already exists")
        }
    }

    suspend fun logoutUser() {
        withContext(Dispatchers.IO) {
            try {
                //Delete Shared Preference
                deleteUserFromCache(context)
                saveIntroPrefs(context)
            } catch (e: Exception) {
                throw Exception(e.localizedMessage)
            }
        }
    }

    suspend fun updateUser(user: User) {
        try {
            var emergencyContactsModel = EmergencyContactsModel(
                username = user.username,
                ec1 = user.emergencyContacts[0],
                ec2 = user.emergencyContacts[1],
                ec3 = user.emergencyContacts[2],
                ec4 = user.emergencyContacts[3],
                ec5 = user.emergencyContacts[4]
            )
            val deferredUser = service.updateEmergencyContacts(emergencyContactsModel)
            Timber.d(deferredUser.await().toString())
            addUserToCache(context, user)
        }catch (e: NoConnectivityException) {
            throw NoConnectivityException()
        }
        catch (e: Exception) {
            throw Exception("Failed to update emergency contacts")
        }
    }

    suspend fun markTrouble(user: User) {
        try {
            var latitude = getLatitudeFromCache(context).toDouble()
            var longitude = getLongitudeFromCache(context).toDouble()
            var trouble =
                Trouble(
                    user.username,
                    latitude = latitude,
                    longitude = longitude,
                    inTrouble = true,
                    emergencyContacts = user.emergencyContacts
                )
            playAudio()
            sendMessage(latitude, longitude, getEmergencyContactFromCache(context))
            val deferredUser = service.markTrouble(trouble)
            val user2 = deferredUser.await()
            Timber.d(user2.toString())
            saveIsInTrouble(context, !getIsInTrouble(context))
        }
        catch (e: Exception) {
//            Timber.d(e)
            context.connectP2P()
            delay(4 * 1000)
            Timber.d(e.localizedMessage)
            throw Exception("Trying to connect to peers!!")
        }
    }

    suspend fun markTroubleP2P(trouble: Trouble) {
        try {
//            var latitude = getLatitudeFromCache(context).toDouble()
//            var longitude = getLongitudeFromCache(context).toDouble()
//            var trouble =
//                Trouble(
//                    user.username,
//                    latitude = latitude,
//                    longitude = longitude,
//                    inTrouble = true,
//                    emergencyContacts = user.emergencyContacts
//                )
//            playAudio()
//            sendMessage(trouble.latitude, trouble., getEmergencyContactFromCache(context))
            val deferredUser = service.markTrouble(trouble)
            val user2 = deferredUser.await()
            Timber.d(user2.toString())
//            saveIsInTrouble(context, !getIsInTrouble(context))
        }
        catch (e: Exception) {
            Timber.d(e.localizedMessage)
            throw Exception("Something went wrong!!")
        }
    }

    suspend fun unMarkTrouble(user: User) {
        try {
            var def = service.unmarkTrouble(Username(user.username))
            Timber.d(def.await().toString())
            saveIsInTrouble(context, !getIsInTrouble(context))
        }catch (e: NoConnectivityException) {
            throw  NoConnectivityException()
        }
        catch (e: Exception) {
            throw Exception("Error unmarking trouble")
        }
    }

    suspend fun checkLogin(user: User) {
        try {
            Timber.d(user.toString())
            user.password = sha256(user.password)
            val deferredUser = service.loginUser(user)
            val user2 = deferredUser.await()
            Timber.d(user2.printUtil())
            saveIsInTrouble(context, user2.inTrouble)
        } catch (e: Exception) {
            throw Exception(e.localizedMessage)
        }
    }

    suspend fun sendOtp(user: User) {
        try {
            val def = service.sendOtp(OTP(user.email!!, user.username))
            val value = def.await()
            Timber.d(value.toString())
        } catch (e: NoConnectivityException) {
            throw Exception(e.localizedMessage)
        } catch (e: Exception) {
            throw Exception("Error sending otp")
        }
    }

    suspend fun verifyOtp(otp: Int, user: User) {
        try {
            var otp = ValidateOTP(user.username, user.email!!, otp)
            Timber.d(otp.toString())
            val def = service.validateOtp(otp)
            val value = def.await()
            Timber.d(value.toString())
        } catch (e: NoConnectivityException) {
            throw Exception("No internet connection")
        } catch (e: Exception) {
            throw Exception("Please enter correct otp")
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

