package com.github.vasniktel.snooper.ui.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.github.vasniktel.snooper.util.ERROR_DELAY_TIME
import com.github.vasniktel.snooper.util.compositeState
import com.github.vasniktel.snooper.util.loadData
import com.github.vasniktel.snooper.util.produceDeferredValue
import kotlinx.coroutines.Dispatchers

class UserListViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _viewState = MutableLiveData<UserListViewState>()
    val viewState: LiveData<UserListViewState> = _viewState

    fun loadData(strategy: UserListRequestStrategy) {
        viewModelScope.loadData(
            mainContext = Dispatchers.Main,
            workContext = Dispatchers.IO,
            loader = { strategy.requestUsers(userRepository) },
            preLoad = { _viewState.value = Loading(true) },
            postLoad = {
                _viewState.value = compositeState(
                    Loading(false),
                    DataLoaded(it)
                )
            },
            onError = {
                _viewState.produceDeferredValue(
                    state = compositeState(
                        Loading(false),
                        DataLoaded(emptyList()),
                        ErrorState("Unable to load data", it)
                    ),
                    time = ERROR_DELAY_TIME,
                    then = { DataLoaded(emptyList()) }
                )
            }
        )
    }
}
