package com.github.vasniktel.snooper.ui.userlist.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.search.SearchQueryBroadcaster
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.github.vasniktel.snooper.ui.userlist.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

private val TAG = UserSearchViewModel::class.simpleName

class UserSearchViewModel(
    private val userRepository: UserRepository,
    private val searchQueryBroadcaster: SearchQueryBroadcaster
) : UserListViewModel() {
    private val searchQuery = MutableLiveData<String>()
    private val observer = Observer<String> {
        updateSearchResult(it)
    }

    init {
        Log.d(TAG, "init")
        searchQuery.observeForever(observer)

        searchQueryBroadcaster.getQuery()
            .onEach {
                Log.d(TAG, "got query: $it")
                searchQuery.value = it
            }
            .onCompletion {
                Log.d(TAG, "onCompletion")
            }
            .launchIn(viewModelScope)
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
            userRepository.search(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchQuery.removeObserver(observer)
    }
}
