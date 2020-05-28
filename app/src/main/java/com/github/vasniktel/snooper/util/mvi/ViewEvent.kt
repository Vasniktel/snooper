package com.github.vasniktel.snooper.util.mvi

interface ViewEvent<T> {
    fun applyCallback(callback: T)
}
