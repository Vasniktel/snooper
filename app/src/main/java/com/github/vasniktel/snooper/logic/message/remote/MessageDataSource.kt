package com.github.vasniktel.snooper.logic.message.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteMessageDataSource {
    @GET("messages/userFeed")
    suspend fun getUserFeed(@Query("id") id: String): List<RemoteMessageDto>

    @GET("messages/userMessages")
    suspend fun getUserMessages(@Query("id") id: String): List<RemoteMessageDto>

    @POST("messages/create")
    suspend fun createMessage(@Body messageDto: RemoteMessageDto)

    @POST("messages/update")
    suspend fun updateMessage(@Body messageDto: RemoteMessageDto)

    @GET("messages/search")
    suspend fun search(@Query("query") query: String): List<RemoteMessageDto>
}
