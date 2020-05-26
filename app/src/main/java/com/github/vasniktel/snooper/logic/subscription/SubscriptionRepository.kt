package com.github.vasniktel.snooper.logic.subscription

interface SubscriptionRepository {
    suspend fun add(followerId: String, followeeId: String)
    suspend fun remove(followerId: String, followeeId: String)
    suspend fun isFollowee(followerId: String, followeeId: String): Boolean
}

class SubscriptionRepositoryImpl(
    private val remote: RemoteSubscriptionDataSource
) : SubscriptionRepository {
    override suspend fun add(followerId: String, followeeId: String) {
        remote.add(followerId, followeeId)
    }

    override suspend fun remove(followerId: String, followeeId: String) {
        remote.remove(followerId, followeeId)
    }

    override suspend fun isFollowee(followerId: String, followeeId: String): Boolean {
        return remote.isFollowee(followerId, followeeId)
    }
}
