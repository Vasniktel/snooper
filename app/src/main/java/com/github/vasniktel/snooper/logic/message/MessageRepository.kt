package com.github.vasniktel.snooper.logic.message

import android.util.Log
import com.dropbox.android.external.store4.*
import com.github.vasniktel.snooper.logic.message.remote.RemoteMessageDataSource
import com.github.vasniktel.snooper.logic.message.remote.RemoteMessageDto
import com.github.vasniktel.snooper.logic.message.remote.toModel
import com.github.vasniktel.snooper.logic.message.remote.toRemoteDto
import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.logic.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

interface MessageRepository {
    suspend fun getFeedForUser(userId: String, fetch: Boolean = false): List<Message>
    suspend fun getUserOwnMessages(userId: String, fetch: Boolean = false): List<Message>
    suspend fun updateMessage(message: Message)
    suspend fun createMessage(message: Message)
    suspend fun addLike(message: Message)
}

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalTime
class MessageRepositoryImpl(
    private val remoteMessageDataSource: RemoteMessageDataSource
) : MessageRepository {
    private val commonMemoryPolicy = MemoryPolicy
        .builder()
        .setExpireAfterAccess(3.minutes)
        .setMemorySize(10)
        .build()

    private val feedStore = StoreBuilder
        .from<String, List<Message>> (
            nonFlowValueFetcher {
                remoteMessageDataSource
                    .getUserFeed(it)
                    .map(RemoteMessageDto::toModel)
            }
        )
        .cachePolicy(commonMemoryPolicy)
        .build()

    private val ownStore = StoreBuilder
        .from<String, List<Message>> (
            nonFlowValueFetcher {
                remoteMessageDataSource
                    .getUserMessages(it)
                    .map(RemoteMessageDto::toModel)
            }
        )
        .cachePolicy(commonMemoryPolicy)
        .build()

    override suspend fun getFeedForUser(userId: String, fetch: Boolean): List<Message> {
        delay(3000)
        return if (fetch) feedStore.fresh(userId) else feedStore.get(userId)
    }

    override suspend fun getUserOwnMessages(userId: String, fetch: Boolean): List<Message> {
        return if (fetch) ownStore.fresh(userId) else ownStore.get(userId)
    }

    override suspend fun updateMessage(message: Message) {
        remoteMessageDataSource.updateMessage(message.toRemoteDto())
    }

    override suspend fun addLike(message: Message) {
        remoteMessageDataSource.updateMessage(message.toRemoteDto().copy(date = message.date))
    }

    override suspend fun createMessage(message: Message) {
        remoteMessageDataSource.createMessage(message.toRemoteDto())
    }
}
