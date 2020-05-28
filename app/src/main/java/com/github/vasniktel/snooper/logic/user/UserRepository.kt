package com.github.vasniktel.snooper.logic.user

import com.dropbox.android.external.store4.*
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.model.toUser
import com.github.vasniktel.snooper.logic.user.local.followees.FolloweesDao
import com.github.vasniktel.snooper.logic.user.local.followees.FolloweesDto
import com.github.vasniktel.snooper.logic.user.local.followees.toModel
import com.github.vasniktel.snooper.logic.user.local.followers.FollowersDao
import com.github.vasniktel.snooper.logic.user.local.followers.FollowersDto
import com.github.vasniktel.snooper.logic.user.local.followers.toModel
import com.github.vasniktel.snooper.logic.user.remote.RemoteUserDataSource
import com.github.vasniktel.snooper.logic.user.remote.RemoteUserDto
import com.github.vasniktel.snooper.logic.user.remote.toModel
import com.github.vasniktel.snooper.logic.user.remote.toRemoteDto
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

interface UserRepository {
    val currentUser: User?
    suspend fun getFollowersOf(userId: String, fetch: Boolean): List<User>
    suspend fun getFolloweesOf(userId: String, fetch: Boolean): List<User>
    suspend fun create(user: User)
    suspend fun getUserById(id: String): User
    suspend fun search(query: String): List<User>
}

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalTime
class UserRepositoryImpl(
    private val remote: RemoteUserDataSource,
    private val followersDao: FollowersDao,
    private val followeesDao: FolloweesDao
) : UserRepository {
    override val currentUser get() = Firebase.auth.currentUser?.toUser()

    private val commonMemoryPolicy = MemoryPolicy
        .builder()
        .setExpireAfterAccess(3.minutes)
        .setMemorySize(10)
        .build()

    private val followersStore = StoreBuilder.from(
        fetcher = nonFlowValueFetcher {
            remote
                .getFollowersOf(it)
                .map(RemoteUserDto::toModel)
        },
        sourceOfTruth = SourceOfTruth.fromNonFlow(
            reader = {
                followersDao.getFollowersOf(it).map(FollowersDto::toModel)
            },
            writer = { followeeId: String, users: List<User> ->
                followersDao.run {
                    deleteFollowersOf(followeeId)
                    insertAll(
                        users.map {
                            FollowersDto.of(followeeId, it)
                        }
                    )
                }
            },
            delete = followersDao::deleteFollowersOf,
            deleteAll = followersDao::deleteAll
        )
    )
        .cachePolicy(commonMemoryPolicy)
        .build()

    private val followeesStore = StoreBuilder.from(
        fetcher = nonFlowValueFetcher {
            remote
                .getFolloweesOf(it)
                .map(RemoteUserDto::toModel)
        },
        sourceOfTruth = SourceOfTruth.fromNonFlow(
            reader = {
                followeesDao.getFolloweesOf(it).map(FolloweesDto::toModel)
            },
            writer = { followerId: String, users: List<User> ->
                followeesDao.run {
                    deleteFolloweesOf(followerId)
                    insertAll(
                        users.map {
                            FolloweesDto.of(followerId, it)
                        }
                    )
                }
            },
            delete = followeesDao::deleteFolloweesOf,
            deleteAll = followeesDao::deleteAll
        )
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

    override suspend fun create(user: User) {
        remote.add(user.toRemoteDto())
    }

    override suspend fun getUserById(id: String): User {
        return remote.getUserById(id).toModel()
    }

    override suspend fun search(query: String): List<User> {
        return remote.search(query).map { it.toModel() }
    }
}
