package com.github.vasniktel.snooper.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.IdpResponse
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.model.toUser
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.github.vasniktel.snooper.util.doWork
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val TAG = LoginViewModel::class.simpleName

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _viewState = MutableLiveData<LoginViewState>()
    val viewState: LiveData<LoginViewState> = _viewState

    init {
        _viewState.value = if (userRepository.currentUser == null) {
            NotAuthorized
        } else {
            Authorized
        }
    }

    fun handleAuthorizationResult(response: IdpResponse?) {
        if (response == null || response.error != null) {
            Log.w(TAG, "Login is unsuccessful: response: $response")
            _viewState.value = AuthorizationError("Unable to authorize", response?.error)
            return
        }

        _viewState.value = Authorized

        if (response.isNewUser) {
            Log.d(TAG, "new user")
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.create(userRepository.currentUser!!)
            }
        }
    }
}
