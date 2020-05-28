package com.github.vasniktel.snooper.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.vasniktel.snooper.logic.search.SearchQueryProducer
import com.github.vasniktel.snooper.util.mvi.MviViewModel

private val TAG = SearchViewModel::class.simpleName

class SearchViewModel(
    private val searchQueryProducer: SearchQueryProducer
) : ViewModel() {
    init {
        Log.d(TAG, "init")
    }

    fun submitQuery(query: String) {
        Log.d(TAG, "submit")
        searchQueryProducer.produce(query)
    }
}
