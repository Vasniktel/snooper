package com.github.vasniktel.snooper.ui.messagelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.github.vasniktel.snooper.util.ERROR_DELAY_TIME
import com.github.vasniktel.snooper.util.compositeState
import com.github.vasniktel.snooper.util.doWork
import com.github.vasniktel.snooper.util.produceDeferredValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class MessageListViewModel : ViewModel() {
    abstract val viewState: LiveData<MessageListViewState>
    abstract fun fetchNewData(strategy: MessageRequestStrategy)
    abstract fun getData(strategy: MessageRequestStrategy)
    abstract fun addLike(message: Message)
}

class MessageListViewModelImpl(
    private val messageRepository: MessageRepository
) : MessageListViewModel() {
    private val _viewState = MutableLiveData<MessageListViewState>(PopulateState)
    override val viewState: LiveData<MessageListViewState> = _viewState

    override fun fetchNewData(strategy: MessageRequestStrategy) {
        loadData {
            strategy.fetchNewData(messageRepository)
        }
    }

    override fun getData(strategy: MessageRequestStrategy) {
        loadData {
            strategy.getData(messageRepository)
        }
    }

    override fun addLike(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.addLike(message)
        }
    }

    private fun loadData(loader: suspend () -> List<Message>) {
        viewModelScope.doWork(
            mainContext = Dispatchers.Main,
            workContext = Dispatchers.IO,
            pre = { _viewState.value = LoadingTopActive(true) },
            worker = loader,
            post = {
                _viewState.value = compositeState(
                    LoadingTopActive(false),
                    LoadedFresh(it)
                )
            },
            onError = {
                _viewState.produceDeferredValue(
                    state = compositeState(
                        LoadingTopActive(false),
                        LoadedFresh(emptyList()),
                        ErrorState("Unable to load data", it)
                    ),
                    time = ERROR_DELAY_TIME,
                    then = { LoadedFresh(emptyList()) }
                )
            }
        )
    }
}
