package com.github.vasniktel.snooper.ui.messagelist.viewmodel

import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.model.User

class MessagePostsViewModel(
    messageRepository: MessageRepository
) : MessageListViewModel(messageRepository) {
    override fun onRefreshEvent(user: User?) {
        loadData {
            messageRepository.getUserOwnMessages(user!!.id, fetch = true)
        }
    }
}
