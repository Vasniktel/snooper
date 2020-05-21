package com.github.vasniktel.snooper.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.github.vasniktel.snooper.util.loadData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class UserFragmentViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _viewState = MutableLiveData<UserViewState>()
    val viewState: LiveData<UserViewState> = _viewState

    val currentUser get() = userRepository.currentUser!!

    fun loadUserById(id: String) {
        if (id == currentUser.id) {
            _viewState.value = LoadedUser(currentUser)
            return
        }

        viewModelScope.loadData(
            mainContext = Dispatchers.Main,
            workContext = Dispatchers.IO,
            loader = { userRepository.getUserById(id) },
            postLoad = { _viewState.value = LoadedUser(it) },
            onError = { _viewState.value = ErrorState("Unable to load user data", it) }
        )
    }
}
