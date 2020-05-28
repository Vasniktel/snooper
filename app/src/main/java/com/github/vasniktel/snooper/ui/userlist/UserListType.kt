package com.github.vasniktel.snooper.ui.userlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class UserListType : Parcelable {
    FOLLOWERS,
    FOLLOWEES,
    SEARCH
}
