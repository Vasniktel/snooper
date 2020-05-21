package com.github.vasniktel.snooper.ui.userlist

import android.os.Parcelable
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.user.UserRepository
import kotlinx.android.parcel.Parcelize

interface UserListRequestStrategy : Parcelable {
    val userId: String
    suspend fun requestUsers(userRepository: UserRepository): List<User>
}

@Parcelize
data class FollowersRequestStrategy(
    override val userId: String
) : UserListRequestStrategy {
    override suspend fun requestUsers(userRepository: UserRepository): List<User> {
        return userRepository.getFollowersOf(userId, fetch = true)
    }
}

@Parcelize
data class FolloweesRequestStrategy(
    override val userId: String
) : UserListRequestStrategy {
    override suspend fun requestUsers(userRepository: UserRepository): List<User> {
        return userRepository.getFolloweesOf(userId, fetch = true)
    }
}
