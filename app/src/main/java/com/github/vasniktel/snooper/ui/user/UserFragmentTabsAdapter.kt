package com.github.vasniktel.snooper.ui.user

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.vasniktel.snooper.ui.messagelist.MessageListFragment
import com.github.vasniktel.snooper.ui.messagelist.PostsRequestStrategy
import com.github.vasniktel.snooper.ui.userlist.FolloweesRequestStrategy
import com.github.vasniktel.snooper.ui.userlist.FollowersRequestStrategy
import com.github.vasniktel.snooper.ui.userlist.UserListFragment
import com.github.vasniktel.snooper.util.SnooperException

class UserFragmentTabsAdapter(
    private val userId: String,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MessageListFragment.create(PostsRequestStrategy(userId))
            1 -> UserListFragment.create(FollowersRequestStrategy(userId))
            2 -> UserListFragment.create(FolloweesRequestStrategy(userId))
            else -> throw SnooperException("Shouldn't have reached here")
        }
    }
}
