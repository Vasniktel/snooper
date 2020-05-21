package com.github.vasniktel.snooper.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

fun <T> CoroutineScope.loadData(
    mainContext: CoroutineContext,
    workContext: CoroutineContext,
    loader: suspend () -> T,
    preLoad: suspend () -> Unit = { },
    postLoad: suspend (T) -> Unit = { },
    onError: suspend (Throwable) -> Unit = { }
) {
    launch(mainContext) {
        preLoad()

        withContext(workContext) {
            try {
                Result.success(loader())
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
        }
            .onSuccess { postLoad(it) }
            .onFailure { onError(it) }
    }
}
