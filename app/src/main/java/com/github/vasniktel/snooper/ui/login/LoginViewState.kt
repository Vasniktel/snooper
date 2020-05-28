package com.github.vasniktel.snooper.ui.login

import com.github.vasniktel.snooper.util.mvi.ViewState

interface LoginViewStateCallback {
    fun onNotAuthorized()
    fun onAuthorized()
    fun onAuthorizationError(message: String, throwable: Throwable?)
}

typealias LoginViewState = ViewState<LoginViewStateCallback>

object NotAuthorized : LoginViewState {
    override fun applyCallback(callback: LoginViewStateCallback) {
        callback.onNotAuthorized()
    }
}

object Authorized : LoginViewState {
    override fun applyCallback(callback: LoginViewStateCallback) {
        callback.onAuthorized()
    }
}

data class AuthorizationError(
    private val message: String,
    private val throwable: Throwable? = null
) : LoginViewState {
    override fun applyCallback(callback: LoginViewStateCallback) {
        callback.onAuthorizationError(message, throwable)
    }
}
