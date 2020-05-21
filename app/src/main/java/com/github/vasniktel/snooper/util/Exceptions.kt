package com.github.vasniktel.snooper.util

import java.lang.RuntimeException

class SnooperException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)
