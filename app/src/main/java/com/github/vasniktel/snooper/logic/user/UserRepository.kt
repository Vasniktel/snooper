package com.github.vasniktel.snooper.logic.user

import com.dropbox.android.external.store4.*
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.model.toUser
import com.github.vasniktel.snooper.logic.user.remote.RemoteUserDataSource
import com.github.vasniktel.snooper.logic.user.remote.RemoteUserDto
import com.github.vasniktel.snooper.logic.user.remote.toModel
import com.github.vasniktel.snooper.logic.user.remote.toRemoteDto
import com.github.vasniktel.snooper.util.SnooperException
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlin.text.get
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

interface UserRepository {
    val currentUser: User?
    suspend fun getFollowersOf(userId: String, fetch: Boolean = false): List<User>
    suspend fun getFolloweesOf(userId: String, fetch: Boolean = false): List<User>
    suspend fun follow(followerId: String, followeeId: String)
    suspend fun create(user: User)
    suspend fun getUserById(id: String): User
}

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalTime
class UserRepositoryImpl(
    private val remoteUserDataSource: RemoteUserDataSource
) : UserRepository {
    override val currentUser get() = Firebase.auth.currentUser?.toUser()

    private val commonMemoryPolicy = MemoryPolicy
        .builder()
        .setExpireAfterAccess(3.minutes)
        .setMemorySize(10)
        .build()

    private val followersStore = StoreBuilder
        .from(
            nonFlowValueFetcher<String, List<User>> {
                remoteUserDataSource
                    .getFollowersOf(it)
                    .map(RemoteUserDto::toModel)
            }
        )
        .cachePolicy(commonMemoryPolicy)
        .build()

    private val followeesStore = StoreBuilder
        .from(
            nonFlowValueFetcher<String, List<User>> {
                remoteUserDataSource
                    .getFolloweesOf(it)
                    .map(RemoteUserDto::toModel)
            }
        )
        .cachePolicy(commonMemoryPolicy)
        .build()

    override suspend fun getFollowersOf(userId: String, fetch: Boolean): List<User> {
        return with(followersStore) {
            if (fetch) fresh(userId) else get(userId)
        }
    }

    override suspend fun getFolloweesOf(userId: String, fetch: Boolean): List<User> {
        return with(followeesStore) {
            if (fetch) fresh(userId) else get(userId)
        }
    }

    override suspend fun follow(followerId: String, followeeId: String) {
        remoteUserDataSource.follow(followerId, followeeId)
    }

    override suspend fun create(user: User) {
        remoteUserDataSource.add(user.toRemoteDto())
    }

    override suspend fun getUserById(id: String): User {
        return remoteUserDataSource.getUserById(id).toModel()
    }

}
