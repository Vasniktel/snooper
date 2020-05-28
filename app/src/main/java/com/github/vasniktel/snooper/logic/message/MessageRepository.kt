package com.github.vasniktel.snooper.logic.message

import com.dropbox.android.external.store4.*
import com.github.vasniktel.snooper.logic.message.local.feed.FeedDao
import com.github.vasniktel.snooper.logic.message.local.feed.FeedDto
import com.github.vasniktel.snooper.logic.message.local.feed.toModel
import com.github.vasniktel.snooper.logic.message.local.posts.PostDto
import com.github.vasniktel.snooper.logic.message.local.posts.PostsDao
import com.github.vasniktel.snooper.logic.message.local.posts.toModel
import com.github.vasniktel.snooper.logic.message.remote.RemoteMessageDataSource
import com.github.vasniktel.snooper.logic.message.remote.RemoteMessageDto
import com.github.vasniktel.snooper.logic.message.remote.toModel
import com.github.vasniktel.snooper.logic.message.remote.toRemoteDto
import com.github.vasniktel.snooper.logic.model.Message
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

interface MessageRepository {
    suspend fun getFeedForUser(userId: String, fetch: Boolean): List<Message>
    suspend fun getUserOwnMessages(userId: String, fetch: Boolean): List<Message>
    suspend fun updateMessage(message: Message)
    suspend fun createMessage(message: Message)
    suspend fun addLike(message: Message)
    suspend fun search(query: String): List<Message>
}

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalTime
class MessageRepositoryImpl(
    private val remoteMessageDataSource: RemoteMessageDataSource,
    private val feedDao: FeedDao,
    private val postsDao: PostsDao
) : MessageRepository {
    private val commonMemoryPolicy = MemoryPolicy
        .builder()
        .setExpireAfterAccess(3.minutes)
        .setMemorySize(10)
        .build()

    private val feedStore = StoreBuilder.from(
            fetcher = nonFlowValueFetcher {
                remoteMessageDataSource
                    .getUserFeed(it)
                    .map(RemoteMessageDto::toModel)
            },
            sourceOfTruth = SourceOfTruth.fromNonFlow(
                reader = {
                    feedDao.getFeedOf(it).map(FeedDto::toModel)
                },
                writer = { feedOwnerId: String, feed: List<Message> ->
                    feedDao.run {
                        deleteFeedOf(feedOwnerId)
                        insertAll(
                            feed.map { FeedDto.of(feedOwnerId, it) }
                        )
                    }
                },
                delete = feedDao::deleteFeedOf,
                deleteAll = feedDao::deleteAll
            )
        )
        .cachePolicy(commonMemoryPolicy)
        .build()

    private val postsStore = StoreBuilder.from(
            fetcher = nonFlowValueFetcher {
                remoteMessageDataSource
                    .getUserMessages(it)
                    .map(RemoteMessageDto::toModel)
            },
            sourceOfTruth = SourceOfTruth.fromNonFlow(
                reader = {
                    postsDao.getPostsOf(it).map(PostDto::toModel)
                },
                writer = { postOwnerId: String, feed: List<Message> ->
                    postsDao.run {
                        deletePostsOf(postOwnerId)
                        insertAll(
                            feed.map { PostDto.of(postOwnerId, it) }
                        )
                    }
                },
                delete = postsDao::deletePostsOf,
                deleteAll = postsDao::deleteAll
            )
        )
        .cachePolicy(commonMemoryPolicy)
        .build()

    override suspend fun getFeedForUser(userId: String, fetch: Boolean): List<Message> {
        return with(feedStore) {
            if (fetch) fresh(userId) else get(userId)
        }
    }

    override suspend fun getUserOwnMessages(userId: String, fetch: Boolean): List<Message> {
        return with(postsStore) {
            if (fetch) fresh(userId) else get(userId)
        }
    }

    override suspend fun updateMessage(message: Message) {
        remoteMessageDataSource.updateMessage(message.toRemoteDto())
    }

    override suspend fun addLike(message: Message) {
        remoteMessageDataSource.updateMessage(
            message.toRemoteDto().copy(
                date = message.date,
                likes = message.likes + 1
            )
        )
    }

    override suspend fun search(query: String): List<Message> {
        return remoteMessageDataSource.search(query).map { it.toModel() }
    }

    override suspend fun createMessage(message: Message) {
        remoteMessageDataSource.createMessage(message.toRemoteDto())
    }
}
