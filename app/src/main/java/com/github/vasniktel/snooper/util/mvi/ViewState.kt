package com.github.vasniktel.snooper.util.mvi

interface ViewState<T> {
    fun applyCallback(callback: T)
}
