package com.github.vasniktel.snooper.ui.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.vasniktel.snooper.ui.messagelist.MessageListFragment
import com.github.vasniktel.snooper.ui.messagelist.MessageListType
import com.github.vasniktel.snooper.ui.navigators.impl.MessageListSearchNavigator
import com.github.vasniktel.snooper.ui.navigators.impl.UserListSearchNavigator
import com.github.vasniktel.snooper.ui.userlist.UserListFragment
import com.github.vasniktel.snooper.ui.userlist.UserListType
import com.github.vasniktel.snooper.util.SnooperException

class SearchFragmentTabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MessageListFragment.create(
                null,
                MessageListSearchNavigator,
                MessageListType.SEARCH
            )
            1 -> UserListFragment.create(
                null,
                UserListSearchNavigator,
                UserListType.SEARCH
            )
            else -> throw SnooperException("Unreachable")
        }
    }
}
