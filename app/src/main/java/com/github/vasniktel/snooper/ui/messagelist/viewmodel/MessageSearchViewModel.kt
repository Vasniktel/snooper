package com.github.vasniktel.snooper.ui.messagelist.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.search.SearchQueryBroadcaster
import com.github.vasniktel.snooper.ui.messagelist.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

private val TAG = MessageSearchViewModel::class.simpleName

class MessageSearchViewModel(
    messageRepository: MessageRepository,
    searchQueryBroadcaster: SearchQueryBroadcaster
) : MessageListViewModel(messageRepository) {
    private val searchQuery = MutableLiveData<String>()
    private val observer = Observer<String> {
        updateSearchResult(it)
    }

    init {
        Log.d(TAG, "init")
        searchQueryBroadcaster.getQuery()
            .onEach {
                Log.d(TAG, "Got query: $it")
                searchQuery.value = it
            }
            .onCompletion {
                Log.d(TAG, "onCompletion")
            }
            .launchIn(viewModelScope)

        searchQuery.observeForever(observer)
    }

    override fun onRefreshEvent(user: User?) {
        val query = searchQuery.value

        if (query == null) {
            _viewState.value = DataState(emptyList())
        } else {
            updateSearchResult(query)
        }
    }

    private fun updateSearchResult(query: String) {
        Log.d(TAG, "updateSearchResult")
        loadData {
            messageRepository.search(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchQuery.removeObserver(observer)
    }
}
