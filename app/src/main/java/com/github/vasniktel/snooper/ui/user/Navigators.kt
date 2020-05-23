package com.github.vasniktel.snooper.ui.user

import androidx.navigation.NavDirections
import com.github.vasniktel.snooper.ui.messagelist.MessageListNavigator
import com.github.vasniktel.snooper.ui.userlist.UserListNavigator
import kotlinx.android.parcel.Parcelize

@Parcelize
object MessageListNavigatorImpl : MessageListNavigator {
    override fun toUserDirection(userId: String): NavDirections {
        return UserFragmentDirections.actionUserFragmentSelf2(userId)
    }
}

@Parcelize
object UserListNavigatorImpl : UserListNavigator {
    override fun toUserDirection(userId: String): NavDirections {
        return UserFragmentDirections.actionUserFragmentSelf2(userId)
    }
}
