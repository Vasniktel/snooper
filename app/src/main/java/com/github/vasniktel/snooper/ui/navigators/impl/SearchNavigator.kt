package com.github.vasniktel.snooper.ui.navigators.impl

import androidx.navigation.NavDirections
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.ui.navigators.MessageListNavigator
import com.github.vasniktel.snooper.ui.navigators.UserListNavigator
import com.github.vasniktel.snooper.ui.navigators.UserNavigator
import com.github.vasniktel.snooper.ui.search.SearchFragmentDirections
import com.github.vasniktel.snooper.ui.user.UserFragmentDirections
import kotlinx.android.parcel.Parcelize

@Parcelize
object MessageListSearchNavigator : MessageListNavigator {
    override fun toUserDirection(user: User): NavDirections {
        return SearchFragmentDirections.actionSearchFragmentToUserFragment(
            user,
            MessageListUserNavigator,
            UserListUserNavigator,
            UserUserNavigator
        )
    }
}

@Parcelize
object UserListSearchNavigator : UserListNavigator {
    override fun toUserDirection(user: User): NavDirections {
        return SearchFragmentDirections.actionSearchFragmentToUserFragment(
            user,
            MessageListUserNavigator,
            UserListUserNavigator,
            UserUserNavigator
        )
    }
}
