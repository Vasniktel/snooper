package com.github.vasniktel.snooper.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.github.vasniktel.snooper.util.doWork
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers

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

        viewModelScope.doWork(
            mainContext = Dispatchers.Main,
            workContext = Dispatchers.IO,
            worker = { userRepository.getUserById(id) },
            post = { _viewState.value = LoadedUser(it) },
            onError = { _viewState.value = ErrorState("Unable to load user data", it) }
        )
    }

    fun logOut() = Firebase.auth.signOut()
}
