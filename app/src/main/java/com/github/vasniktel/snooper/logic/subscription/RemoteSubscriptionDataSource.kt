package com.github.vasniktel.snooper.logic.subscription

import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteSubscriptionDataSource {
    @GET("subscriptions/add")
    suspend fun add(
        @Query("followerId") followerId: String,
        @Query("followeeId") followeeId: String
    )

    @GET("subscriptions/remove")
    suspend fun remove(
        @Query("followerId") followerId: String,
        @Query("followeeId") followeeId: String
    )

    @GET("subscriptions/isFollowee")
    suspend fun isFollowee(
        @Query("followerId") followerId: String,
        @Query("followeeId") followeeId: String
    ): Boolean
}
