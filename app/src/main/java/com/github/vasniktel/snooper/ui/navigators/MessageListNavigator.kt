package com.github.vasniktel.snooper.ui.navigators

import android.os.Parcelable
import androidx.navigation.NavDirections
import com.github.vasniktel.snooper.logic.model.User

interface MessageListNavigator : Parcelable {
    fun toUserDirection(user: User): NavDirections
}
