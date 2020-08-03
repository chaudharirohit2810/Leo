package com.rohit2810.leo_kotlin.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rohit2810.leo_kotlin.NetworkUtils.NetworkConnectionInterceptor
import com.rohit2810.leo_kotlin.models.user.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://peaceful-refuge-01419.herokuapp.com/LeoHelp/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


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
    @POST("user/add")
    fun createUser(@Body user: User): Deferred<User>

    //for Login
    @POST("user/login")
    fun loginUser(@Body user: User): Deferred<User>

    //Mark Trouble
    @PUT("user/mark_trouble")
    fun markTrouble(@Body trouble: Trouble): Deferred<Any>

    @PUT("user/unmark_trouble")
    fun unmarkTrouble(@Body username: UnMarkTrouble): Deferred<Any>

    @POST("otp")
    fun sendOtp(@Body otp: OTP): Deferred<Any>

    @POST("validate/otp")
    fun validateOtp(@Body validateOTP: ValidateOTP): Deferred<Any>

    @POST("user/exists")
    fun validateUserName(@Body username: Username): Deferred<Any>

    @PUT("user/update/emergency_contacts")
    fun updateEmergencyContacts(@Body emergencyContactsModel: EmergencyContactsModel): Deferred<Any>

    @PUT("user/recreate_password")
    fun forgotPassword(@Body user: ChangePassword): Deferred<Any>

}