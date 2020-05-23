package com.github.vasniktel.snooper.ui.feed

import androidx.navigation.NavDirections
import com.github.vasniktel.snooper.ui.messagelist.MessageListNavigator
import kotlinx.android.parcel.Parcelize

@Parcelize
object MessageListNavigatorImpl : MessageListNavigator {
    override fun toUserDirection(userId: String): NavDirections {
        return FeedFragmentDirections.actionFeedFragmentToUserFragment(userId)
    }
}
