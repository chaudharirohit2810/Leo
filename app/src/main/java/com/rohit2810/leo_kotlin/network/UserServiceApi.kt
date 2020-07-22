package com.rohit2810.leo_kotlin.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rohit2810.leo_kotlin.NetworkUtils.NetworkConnectionInterceptor
import com.rohit2810.leo_kotlin.models.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://peaceful-refuge-01419.herokuapp.com/LeoHelp/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .addCallAdapterFactory(CoroutineCallAdapterFactory())
//    .baseUrl(BASE_URL)
//    .build()

object UserApi {
    fun retrofitService(context: Context): UserServiceApi {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(NetworkConnectionInterceptor(context))
        val retrofit = Retrofit.Builder()
            .client(okHttpClient.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()
        return retrofit.create(UserServiceApi::class.java)
    }
}


interface UserServiceApi{
    //To add new user
    @POST("addUser")
    fun createUser(@Body user: User): Deferred<User>

    //for Login
    @POST("loginIn")
    fun loginUser(@Body user: User): Deferred<User>

    //Mark Trouble
    @PUT("markTrouble")
    fun markTrouble(@Body trouble: Trouble): Deferred<Any>

    @PUT("unmarkTrouble")
    fun unmarkTrouble(@Body username: UnMarkTrouble): Deferred<Any>

    @POST("otp")
    fun sendOtp(@Body otp: OTP): Deferred<Any>

    @POST("validateOTP")
    fun validateOtp(@Body validateOTP: ValidateOTP): Deferred<Any>

    @POST("user_nameExists")
    fun validateUserName(@Body username: Username): Deferred<Any>

    @PUT("updateEC")
    fun updateEmergencyContacts(@Body emergencyContactsModel: EmergencyContactsModel): Deferred<Any>

}