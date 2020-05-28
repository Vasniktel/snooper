package com.github.vasniktel.snooper.ui.userlist.viewmodel

import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.logic.user.UserRepository

class UserFollowersViewModel(
    private val userRepository: UserRepository
) : UserListViewModel() {
    override fun onRefreshEvent(user: User?) {
        loadData {
            userRepository.getFollowersOf(user!!.id, fetch = true)
        }
    }
}
