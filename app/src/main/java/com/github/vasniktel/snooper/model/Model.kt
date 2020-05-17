package com.github.vasniktel.snooper.model

import java.util.*

sealed class User {
    data class LoggedIn(
        val id: String,
        val email: String,
        val firstName: String,
        val secondName: String,
        val iconUrl: String?
    ) : User()

    object Anonymous : User()

    val isAnonymous get() = this is Anonymous
}

data class UserFollowees(
    val userId: String,
    val followeeId: List<String>
)

data class UserFollowers(
    val userId: String,
    val followerId: List<String>
)

data class LocationMessage(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val ownerId: String,
    val viewerId: List<String>,
    val likes: Int,
    val date: Date
) {
    init {
        require(likes >= 0)
    }
}
