package com.github.vasniktel.snooper.ui.user

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.ui.messagelist.MessageListFragment
import com.github.vasniktel.snooper.ui.messagelist.MessageListType
import com.github.vasniktel.snooper.ui.userlist.UserListFragment
import com.github.vasniktel.snooper.ui.userlist.UserListType
import com.github.vasniktel.snooper.util.SnooperException

class UserFragmentTabsAdapter(
    private val user: User,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MessageListFragment.create(
                user,
                MessageListNavigatorImpl,
                MessageListType.POSTS
            )
            1 -> UserListFragment.create(
                user,
                UserListNavigatorImpl,
                UserListType.FOLLOWERS
            )
            2 -> UserListFragment.create(
                user,
                UserListNavigatorImpl,
                UserListType.FOLLOWEES
            )
            else -> throw SnooperException("Shouldn't have reached here")
        }
    }
}
