package com.github.vasniktel.snooper.logic.search

import kotlinx.coroutines.flow.Flow

interface SearchQueryBroadcaster {
    fun getQuery(): Flow<String>
}
