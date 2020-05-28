package com.github.vasniktel.snooper.ui.navigators.impl

import androidx.navigation.NavDirections
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.ui.navigators.MessageListNavigator
import com.github.vasniktel.snooper.ui.navigators.UserListNavigator
import com.github.vasniktel.snooper.ui.navigators.UserNavigator
import com.github.vasniktel.snooper.ui.user.UserFragmentDirections
import kotlinx.android.parcel.Parcelize

@Parcelize
object UserUserNavigator : UserNavigator {
    override fun toLoginDirection(): NavDirections {
        return UserFragmentDirections.actionUserFragmentToLoginFragment()
    }
}

@Parcelize
object MessageListUserNavigator : MessageListNavigator {
    override fun toUserDirection(user: User): NavDirections {
        return UserFragmentDirections.actionUserFragmentSelf(
            user,
            this,
            UserListUserNavigator,
            UserUserNavigator
        )
    }
}

@Parcelize
object UserListUserNavigator : UserListNavigator {
    override fun toUserDirection(user: User): NavDirections {
        return UserFragmentDirections.actionUserFragmentSelf(
            user,
            MessageListUserNavigator,
            this,
            UserUserNavigator
        )
    }
}
