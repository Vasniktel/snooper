package com.github.vasniktel.snooper.ui.messagelist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MessageListType : Parcelable {
    POSTS,
    FEED,
    SEARCH
}
