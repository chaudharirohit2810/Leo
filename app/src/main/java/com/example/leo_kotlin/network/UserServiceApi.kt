package com.example.leo_kotlin.network

import com.example.leo_kotlin.models.User
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

private const val BASE_URL = "https://peaceful-refuge-01419.herokuapp.com/LeoHelp/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl("https://peaceful-refuge-01419.herokuapp.com/LeoHelp/")
    .build()

object UserApi {
    val retrofitService: UserServiceApi by lazy {
        retrofit.create(UserServiceApi::class.java)
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
    fun createTrouble(@Body user: User): Deferred<User>
}