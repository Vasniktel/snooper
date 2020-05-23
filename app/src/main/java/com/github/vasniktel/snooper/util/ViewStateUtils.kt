package com.github.vasniktel.snooper.util

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

suspend fun <T> MutableLiveData<T>.produceDeferredValue(
    state: T,
    time: Long,
    then: suspend () -> T
) {
    withContext(Dispatchers.Main) {
        value = state
        delay(time)
        if (value == state) {
            value = then()
        }
    }
}
