package com.github.vasniktel.snooper.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun signUp(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.create(user)
        }
    }
}
