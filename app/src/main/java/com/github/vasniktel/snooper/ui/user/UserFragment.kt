package com.github.vasniktel.snooper.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.util.changeVisibility
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private val TAG = UserFragment::class.simpleName
private val TAB_NAME = arrayOf("POSTS", "FOLLOWERS", "FOLLOWEES")

class UserFragment : Fragment(), UserViewStateCallback {
    private val args: UserFragmentArgs by navArgs()
    private val viewModel: UserFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userId = args.userId ?: viewModel.currentUser.id

        viewPager.adapter = UserFragmentTabsAdapter(userId, this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = TAB_NAME[position]
        }.attach()

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            requireActivity().recreate()
        }

        val isCurrentUser = args.userId == null
        changeVisibility(backButton, !isCurrentUser)
        changeVisibility(logoutButton, isCurrentUser)

        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.applyCallback(this)
        })

        viewModel.loadUserById(userId)
    }

    override fun onUserLoaded(user: User) {
        userName.text = user.name
    }

    override fun onError(message: String, throwable: Throwable?) {
        Log.d(TAG, "Got error: $message", throwable)
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        fun create(userId: String? = null) =
            UserFragment().apply {
                arguments = UserFragmentArgs(userId).toBundle()
            }
    }
}