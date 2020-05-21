package com.github.vasniktel.snooper.ui.myprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.ui.user.UserFragment
import com.github.vasniktel.snooper.ui.user.UserFragmentArgs

class MyProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
            .add(R.id.userFragmentContainer, UserFragment.create())
            .commit()
    }
}