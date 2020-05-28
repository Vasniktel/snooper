package com.github.vasniktel.snooper.ui.userlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.ui.navigators.UserListNavigator
import com.github.vasniktel.snooper.ui.userlist.viewmodel.UserListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_user_list.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.qualifier.named

private val TAG = UserListFragment::class.simpleName

class UserListFragment : Fragment(), UserListViewStateCallback {
    private lateinit var navigator: UserListNavigator
    private lateinit var adapter: UserListAdapter
    private lateinit var viewModel: UserListViewModel
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            user = it[USER_KEY] as User?
            navigator = it[NAVIGATOR_KEY] as UserListNavigator
            viewModel = getViewModel(named(it[TYPE_KEY] as UserListType))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = UserListAdapter { user, _ ->
            findNavController().navigate(navigator.toUserDirection(user))
        }

        userList.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.onEvent(RefreshEvent(user))
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            Log.d(TAG, "Received state: $it")
            it.applyCallback(this)
        }
    }

    override fun onLoadingState() {
        refreshLayout.isRefreshing = true
    }

    override fun onDataState(data: List<User>) {
        noUserText.isVisible = data.isEmpty()
        userList.isVisible = data.isNotEmpty()
        refreshLayout.isRefreshing = false
        adapter.submitList(data)
    }

    override fun onErrorState(message: String, throwable: Throwable?) {
        Log.e(TAG, "An error occurred: '$message'", throwable)
        Snackbar.make(
            requireParentFragment().requireView(),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onPopulateState() {
        viewModel.onEvent(RefreshEvent(user))
    }

    companion object {
        private const val USER_KEY = "userListUserKey"
        private const val TYPE_KEY = "userListTypeKey"
        private const val NAVIGATOR_KEY = "userListNavigatorKey"

        fun create(
            user: User?,
            navigator: UserListNavigator,
            type: UserListType
        ) = UserListFragment().apply {
            arguments = bundleOf(
                USER_KEY to user,
                NAVIGATOR_KEY to navigator,
                TYPE_KEY to type
            )
        }
    }
}