package com.github.vasniktel.snooper.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

fun <T> CoroutineScope.doWork(
    mainContext: CoroutineContext,
    workContext: CoroutineContext,
    worker: suspend () -> T,
    pre: suspend () -> Unit = { },
    post: suspend (T) -> Unit = { },
    onError: suspend (Throwable) -> Unit = { }
) {
    launch(mainContext) {
        pre()

        withContext(workContext) {
            try {
                Result.success(worker())
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
        }
            .onSuccess { post(it) }
            .onFailure { onError(it) }
    }
}
