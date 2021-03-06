package com.github.vasniktel.snooper.ui.user

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
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

    @SuppressLint("MissingPermission")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user = args.user ?: viewModel.currentUser

        userViewPager.adapter = UserFragmentTabsAdapter(
            user,
            this,
            args.messageListNavigator,
            args.userListNavigator
        )
        TabLayoutMediator(userTabLayout, userViewPager) { tab, position ->
            tab.text = TAB_NAME[position]
        }.attach()

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        logoutButton.setOnClickListener {
            viewModel.onEvent(LogOutEvent)
            findNavController().navigate(args.userNavigator.toLoginDirection())
        }

        postMessageButton.setOnClickListener {
            runWithPermissions(ACCESS_FINE_LOCATION) {
                viewModel.onEvent(PostMessageEvent)
            }
        }

        backButton.isVisible = args.user != null
        logoutButton.isVisible = user == viewModel.currentUser
        postMessageButton.isVisible = user == viewModel.currentUser
        subscriptionButton.isVisible = false

        viewModel.viewState.observe(viewLifecycleOwner) {
            Log.d(TAG, "Received state: $it")
            it.applyCallback(this)
        }

        userName.text = user.name
        Picasso.get()
            // Picasso doesn't load vector drawables if url is null
            .load(user.photoUrl ?: "load error drawable")
            .error(R.drawable.ic_baseline_person_24)
            .resizeDimen(R.dimen.user_fragment_photo, R.dimen.user_fragment_photo)
            .transform(CropCircleTransformation())
            .into(userPhoto)
    }

    override fun onSubscriptionUpdateState(isFollowee: Boolean) {
        subscriptionButton.apply {
            isVisible = true
            text = getString(
                if (isFollowee) {
                    R.string.unfollow_button_text
                } else {
                    R.string.follow_button_text
                }
            )
            setOnClickListener {
                viewModel.onEvent(ChangeSubscriptionsEvent(args.user!!, isFollowee))
            }
        }
    }

    override fun onPopulateState() {
        args.user?.let {
            if (it != viewModel.currentUser) {
                viewModel.onEvent(UpdateSubscriptionEvent(it))
            }
        }
    }

    override fun onError(message: String, throwable: Throwable?) {
        Log.w(TAG, "Got error: $message", throwable)
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        fun create(args: UserFragmentArgs) =
            UserFragment().apply {
                arguments = args.toBundle()
            }
    }
}