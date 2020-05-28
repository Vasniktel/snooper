package com.github.vasniktel.snooper.ui.userlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.github.vasniktel.snooper.ui.userlist.*
import com.github.vasniktel.snooper.util.doWork
import com.github.vasniktel.snooper.util.mvi.MviViewModel
import kotlinx.coroutines.Dispatchers

abstract class UserListViewModel
    : ViewModel(), MviViewModel<UserListViewEvent, UserListViewState>, UserListViewEventCallback {
    protected val _viewState = MutableLiveData<UserListViewState>(
        PopulateState
    )
    override val viewState: LiveData<UserListViewState> = _viewState

    protected fun loadData(loader: suspend () -> List<User>) {
        viewModelScope.doWork(
            mainContext = Dispatchers.Main,
            workContext = Dispatchers.IO,
            worker = loader,
            pre = { _viewState.value =
                LoadingState
            },
            post = { _viewState.value =
                DataState(it)
            },
            onError = {
                _viewState.apply {
                    value = ErrorState(
                        "Unable to load data",
                        it
                    )
                    value = DataState(
                        emptyList()
                    )
                }
            }
        )
    }

    override fun onEvent(event: UserListViewEvent) {
        event.applyCallback(this)
    }
}
