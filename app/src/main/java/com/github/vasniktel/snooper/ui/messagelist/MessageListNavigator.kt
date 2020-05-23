package com.github.vasniktel.snooper.ui.messagelist

import android.os.Parcelable
import androidx.navigation.NavDirections

interface MessageListNavigator : Parcelable {
    fun toUserDirection(userId: String): NavDirections
}
