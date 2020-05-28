package com.github.vasniktel.snooper.ui.myprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.ui.navigators.impl.MessageListMyProfileNavigator
import com.github.vasniktel.snooper.ui.navigators.impl.UserListMyProfileNavigator
import com.github.vasniktel.snooper.ui.navigators.impl.UserMyProfileNavigator
import com.github.vasniktel.snooper.ui.user.UserFragment
import com.github.vasniktel.snooper.ui.user.UserFragmentArgs

class MyProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        childFragmentManager.run {
            if (findFragmentById(R.id.userFragmentContainer) == null) {
                commit {
                    add(
                        R.id.userFragmentContainer,
                        UserFragment.create(
                            UserFragmentArgs(
                                messageListNavigator = MessageListMyProfileNavigator,
                                userListNavigator = UserListMyProfileNavigator,
                                userNavigator = UserMyProfileNavigator
                            )
                        )
                    )
                }
            }
        }
    }
}