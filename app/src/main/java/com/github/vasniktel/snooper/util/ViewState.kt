package com.github.vasniktel.snooper.util

interface ViewState<T> {
    fun applyCallback(callback: T)
}

data class CompositeState<T>(
    private val states: List<ViewState<T>>
) : ViewState<T> {
    override fun applyCallback(callback: T) {
        for (state in states) {
            state.applyCallback(callback)
        }
    }
}

fun <T> compositeState(vararg state: ViewState<T>) = CompositeState(state.asList())
