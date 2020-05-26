package com.github.vasniktel.snooper.ui.user

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
import com.google.android.material.tabs.TabLayoutMediator
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user = args.user ?: viewModel.currentUser

        viewPager.adapter = UserFragmentTabsAdapter(user.id, this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = TAB_NAME[position]
        }.attach()

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        logoutButton.setOnClickListener {
            viewModel.logOut()
            findNavController().navigate(
                UserFragmentDirections.actionUserFragmentToLoginFragment()
            )
        }

        backButton.isVisible = args.user != null
        logoutButton.isVisible = user == viewModel.currentUser
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

    override fun onSubscriptionUpdate(isFollowee: Boolean) {
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
                viewModel.changeSubscription(args.user!!, isFollowee)
            }
        }
    }

    override fun onPopulateState() {
        args.user?.let {
            if (it != viewModel.currentUser) {
                viewModel.updateSubscriptionStatus(args.user!!)
            }
        }
    }
}