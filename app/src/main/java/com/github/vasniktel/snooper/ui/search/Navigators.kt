package com.github.vasniktel.snooper.ui.search

import androidx.navigation.NavDirections
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.ui.messagelist.MessageListNavigator
import com.github.vasniktel.snooper.ui.userlist.UserListNavigator
import kotlinx.android.parcel.Parcelize

@Parcelize
object MessageListNavigatorImpl : MessageListNavigator {
    override fun toUserDirection(user: User): NavDirections {
        return SearchFragmentDirections.actionSearchFragmentToUserFragment(user)
    }
}

@Parcelize
object UserListNavigatorImpl : UserListNavigator {
    override fun toUserDirection(user: User): NavDirections {
        return SearchFragmentDirections.actionSearchFragmentToUserFragment(user)
    }
}
