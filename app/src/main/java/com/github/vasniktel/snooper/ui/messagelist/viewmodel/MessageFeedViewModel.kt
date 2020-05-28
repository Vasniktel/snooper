package com.github.vasniktel.snooper.ui.messagelist.viewmodel

import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.user.UserRepository

class MessageFeedViewModel(
    messageRepository: MessageRepository
) : MessageListViewModel(messageRepository) {
    override fun onRefreshEvent(user: User?, fetch: Boolean) {
        loadData {
            messageRepository.getFeedForUser(user!!.id, fetch)
        }
    }
}
