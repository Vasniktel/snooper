package com.github.vasniktel.snooper.ui.navigators.impl

import androidx.navigation.NavDirections
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.ui.myprofile.MyProfileFragmentDirections
import com.github.vasniktel.snooper.ui.navigators.MessageListNavigator
import com.github.vasniktel.snooper.ui.navigators.UserListNavigator
import com.github.vasniktel.snooper.ui.navigators.UserNavigator
import com.github.vasniktel.snooper.ui.user.UserFragmentDirections
import kotlinx.android.parcel.Parcelize

@Parcelize
object MessageListMyProfileNavigator : MessageListNavigator {
    override fun toUserDirection(user: User): NavDirections {
        return MyProfileFragmentDirections.actionMyProfileFragmentToUserFragment(
            user,
            this,
            UserListMyProfileNavigator,
            UserMyProfileNavigator
        )
    }
}

@Parcelize
object UserListMyProfileNavigator : UserListNavigator {
    override fun toUserDirection(user: User): NavDirections {
        return MyProfileFragmentDirections.actionMyProfileFragmentToUserFragment(
            user,
            MessageListMyProfileNavigator,
            this,
            UserMyProfileNavigator
        )
    }
}

@Parcelize
object UserMyProfileNavigator : UserNavigator {
    override fun toLoginDirection(): NavDirections {
        return UserFragmentDirections.actionUserFragmentToLoginFragment()
    }
}
