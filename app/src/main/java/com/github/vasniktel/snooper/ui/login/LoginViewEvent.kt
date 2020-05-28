package com.github.vasniktel.snooper.ui.login

import com.firebase.ui.auth.IdpResponse
import com.github.vasniktel.snooper.util.mvi.ViewEvent

interface LoginViewEventCallback {
    fun onAuthorizationComplete(result: IdpResponse?)
}

typealias LoginViewEvent = ViewEvent<LoginViewEventCallback>

data class AuthorizationComplete(
    private val response: IdpResponse?
) : LoginViewEvent {
    override fun applyCallback(callback: LoginViewEventCallback) {
        callback.onAuthorizationComplete(response)
    }
}
