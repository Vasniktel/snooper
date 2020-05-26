package com.github.vasniktel.snooper.ui.user

import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.location.LocationProvider
import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.subscription.SubscriptionRepository
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.github.vasniktel.snooper.util.SnooperException
import com.github.vasniktel.snooper.util.doWork
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers

class UserFragmentViewModel(
    private val userRepository: UserRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val locationProvider: LocationProvider,
    private val messageRepository: MessageRepository
) : ViewModel() {
    val currentUser get() = userRepository.currentUser!!
    fun logOut() = Firebase.auth.signOut()

    private val _viewState = MutableLiveData<UserViewState>(PopulateState)
    val viewState: LiveData<UserViewState> = _viewState

    fun changeSubscription(user: User, isFollowee: Boolean) {
        if (user == currentUser) {
            throw SnooperException("Can't subscribe to myself")
        }

        viewModelScope.doWork(
            mainContext = Dispatchers.Main,
            workContext = Dispatchers.IO,
            worker = {
                if (isFollowee) {
                    subscriptionRepository.remove(currentUser.id, user.id)
                } else {
                    subscriptionRepository.add(currentUser.id, user.id)
                }
            },
            post = {
                _viewState.value = SubscriptionUpdate(!isFollowee)
            }
        )
    }

    fun updateSubscriptionStatus(user: User) {
        if (user == currentUser) {
            throw SnooperException("Attempt to fetch subscription status of myself")
        }

        viewModelScope.doWork(
            mainContext = Dispatchers.Main,
            workContext = Dispatchers.IO,
            worker = { subscriptionRepository.isFollowee(currentUser.id, user.id) },
            post = {
                _viewState.value = SubscriptionUpdate(it)
            }
        )
    }

    @RequiresPermission(anyOf = [
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.ACCESS_FINE_LOCATION"
    ])
    fun postMessage() {
        viewModelScope.doWork(
            mainContext = Dispatchers.Main,
            workContext = Dispatchers.IO,
            worker = {
                val location = locationProvider.getLocation()

                messageRepository.createMessage(
                    Message.newInstance(currentUser, location, null)
                )
            },
            onError = {
                _viewState.value =
                    ErrorState("Failed to post a message", it)
            }
        )
    }
}
