package com.github.vasniktel.snooper.util.mvi

import androidx.lifecycle.LiveData

interface MviViewModel<in E : ViewEvent<*>, S : ViewState<*>> {
    val viewState: LiveData<S>
    fun onEvent(event: E)
}
