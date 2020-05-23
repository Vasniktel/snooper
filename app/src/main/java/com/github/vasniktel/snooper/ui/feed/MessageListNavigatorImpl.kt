package com.github.vasniktel.snooper.ui.feed

import androidx.navigation.NavDirections
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.ui.messagelist.MessageListNavigator
import kotlinx.android.parcel.Parcelize

@Parcelize
object MessageListNavigatorImpl : MessageListNavigator {
    override fun toUserDirection(user: User): NavDirections {
        return FeedFragmentDirections.actionFeedFragmentToUserFragment(user)
    }
}
