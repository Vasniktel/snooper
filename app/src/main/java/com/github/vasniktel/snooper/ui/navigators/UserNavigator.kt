package com.github.vasniktel.snooper.ui.navigators

import android.os.Parcelable
import androidx.navigation.NavDirections

interface UserNavigator : Parcelable {
    fun toLoginDirection(): NavDirections
}
