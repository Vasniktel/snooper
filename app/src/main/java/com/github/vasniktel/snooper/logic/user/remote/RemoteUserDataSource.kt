package com.github.vasniktel.snooper.logic.user.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteUserDataSource {
    @GET("users/followees")
    suspend fun getFolloweesOf(@Query("id") id: String): List<RemoteUserDto>

    @GET("users/followers")
    suspend fun getFollowersOf(@Query("id") id: String): List<RemoteUserDto>

    @GET("users/get")
    suspend fun getUserById(@Query("id") id: String): RemoteUserDto

    @POST("users/add")
    suspend fun add(@Body user: RemoteUserDto)
}
