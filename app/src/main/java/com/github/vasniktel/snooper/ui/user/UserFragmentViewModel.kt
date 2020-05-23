package com.github.vasniktel.snooper.ui.user

import androidx.lifecycle.ViewModel
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserFragmentViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    val currentUser get() = userRepository.currentUser!!
    fun logOut() = Firebase.auth.signOut()
}
