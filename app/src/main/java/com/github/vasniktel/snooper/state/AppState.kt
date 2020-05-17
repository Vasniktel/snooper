package com.github.vasniktel.snooper.state

import com.github.vasniktel.snooper.model.User

interface AppState {
    val currentUser: User
}
