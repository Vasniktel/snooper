package com.github.vasniktel.snooper.ui.userlist

import android.os.Parcelable
import androidx.navigation.NavDirections

interface UserListNavigator : Parcelable {
    fun toUserDirection(userId: String): NavDirections
}
