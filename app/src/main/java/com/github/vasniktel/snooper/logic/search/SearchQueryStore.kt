package com.github.vasniktel.snooper.logic.search

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

@FlowPreview
@ExperimentalCoroutinesApi
class SearchQueryStore : SearchQueryBroadcaster, SearchQueryProducer {
    private val channel by lazy {
        BroadcastChannel<String>(10)
    }

    override fun getQuery(): Flow<String> {
        return channel.asFlow()
    }

    override fun produce(query: String) {
        channel.offer(query)
    }
}
