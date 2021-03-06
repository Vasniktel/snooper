package com.github.vasniktel.snooper.ui.navigators.impl

import androidx.navigation.NavDirections
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.ui.feed.FeedFragmentDirections
import com.github.vasniktel.snooper.ui.navigators.MessageListNavigator
import com.github.vasniktel.snooper.ui.navigators.UserListNavigator
import com.github.vasniktel.snooper.ui.navigators.UserNavigator
import com.github.vasniktel.snooper.ui.user.UserFragmentDirections
import kotlinx.android.parcel.Parcelize

@Parcelize
object MessageListFeedNavigator : MessageListNavigator {
    override fun toUserDirection(user: User): NavDirections {
        return FeedFragmentDirections.actionFeedFragmentToUserFragment(
            user,
            MessageListUserNavigator,
            UserListUserNavigator,
            UserUserNavigator
        )
    }
}
