package com.github.vasniktel.snooper.ui.feed

import androidx.lifecycle.ViewModel
import com.github.vasniktel.snooper.logic.user.UserRepository

class FeedFragmentViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    val currentUser get() = userRepository.currentUser!!
}