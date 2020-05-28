package com.github.vasniktel.snooper.ui.messagelist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.search.SearchQueryBroadcaster
import com.github.vasniktel.snooper.ui.messagelist.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MessageSearchViewModel(
    messageRepository: MessageRepository,
    searchQueryBroadcaster: SearchQueryBroadcaster
) : MessageListViewModel(messageRepository) {
    private val searchQuery = MutableLiveData<String>()
    private val observer = Observer<String> {
        updateSearchResult(it)
    }

    init {
        searchQueryBroadcaster.getQuery()
            .onEach {
                searchQuery.value = it
            }
            .launchIn(viewModelScope)

        searchQuery.observeForever(observer)
    }

    override fun onRefreshEvent(user: User?, fetch: Boolean) {
        val query = searchQuery.value

        if (query == null) {
            _viewState.value = DataState(emptyList())
        } else {
            updateSearchResult(query)
        }
    }

    private fun updateSearchResult(query: String) {
        loadData {
            messageRepository.search(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchQuery.removeObserver(observer)
    }
}
