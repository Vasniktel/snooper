package com.github.vasniktel.snooper.util.mvi

interface ViewEventSource<T : ViewEvent<*>> {
    var eventCallback: ((T) -> Unit)?
}
