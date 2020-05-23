package com.github.vasniktel.snooper.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.util.changeVisibility
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.user_list_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private val TAB_NAME = arrayOf("POSTS", "FOLLOWERS", "FOLLOWEES")

class UserFragment : Fragment() {
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

        viewPager.adapter = UserFragmentTabsAdapter(user, this)
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

        changeVisibility(backButton, args.user != null)
        changeVisibility(logoutButton, args.user == null)

        userName.text = user.name

        Picasso.get()
            // Picasso doesn't load vector drawables if url is null
            .load(user.photoUrl ?: "load error drawable")
            .error(R.drawable.ic_baseline_person_24)
            .resizeDimen(R.dimen.user_fragment_photo, R.dimen.user_fragment_photo)
            .transform(CropCircleTransformation())
            .into(userPhoto)
    }
}