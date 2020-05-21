package com.github.vasniktel.snooper.ui.messagelist

import android.os.Parcelable
import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.model.Message
import kotlinx.android.parcel.Parcelize

interface MessageRequestStrategy : Parcelable {
    val userId: String
    suspend fun fetchNewData(messageRepository: MessageRepository): List<Message>
    suspend fun getData(messageRepository: MessageRepository): List<Message>
}

@Parcelize
data class FeedRequestStrategy(
    override val userId: String
) : MessageRequestStrategy {
    override suspend fun fetchNewData(messageRepository: MessageRepository): List<Message> {
        return messageRepository.getFeedForUser(userId, fetch = true)
    }

    override suspend fun getData(messageRepository: MessageRepository): List<Message> {
        return messageRepository.getFeedForUser(userId, fetch = false)
    }
}

@Parcelize
data class PostsRequestStrategy(
    override val userId: String
) : MessageRequestStrategy {
    override suspend fun fetchNewData(messageRepository: MessageRepository): List<Message> {
        return messageRepository.getUserOwnMessages(userId, fetch = true)
    }

    override suspend fun getData(messageRepository: MessageRepository): List<Message> {
        return messageRepository.getUserOwnMessages(userId, fetch = false)
    }
}
