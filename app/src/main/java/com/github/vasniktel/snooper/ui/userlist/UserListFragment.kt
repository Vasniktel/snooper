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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_user_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private val TAG = UserListFragment::class.simpleName

class UserListFragment : Fragment(), UserListViewStateCallback {
    private lateinit var navigator: UserListNavigator
    private lateinit var strategy: UserListRequestStrategy
    private lateinit var adapter: UserListAdapter
    private var snackBar: Snackbar? = null
    private val viewModel: UserListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            strategy = it[STRATEGY_KEY] as UserListRequestStrategy
            navigator = it[NAVIGATOR_KEY] as UserListNavigator
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
            viewModel.loadData(strategy)
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            Log.d(TAG, "Received state: $it")
            it.applyCallback(this)
        }
    }

    override fun onLoadingVisibilityChange(visible: Boolean) {
        refreshLayout.isRefreshing = visible
    }

    override fun onDataLoaded(data: List<User>) {
        noUserText.isVisible = data.isEmpty()
        userList.isVisible = data.isNotEmpty()
        adapter.submitList(data)
        snackBar?.dismiss()
    }

    override fun onError(message: String, throwable: Throwable?) {
        Log.e(TAG, "An error occurred: '$message'", throwable)
        snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
        snackBar?.show()
    }

    override fun onPopulateState() {
        viewModel.loadData(strategy)
    }

    companion object {
        private const val STRATEGY_KEY = "userListRequestStrategy"
        private const val NAVIGATOR_KEY = "userListNavigator"

        fun create(
            strategy: UserListRequestStrategy,
            navigator: UserListNavigator
        ) = UserListFragment().apply {
            arguments = bundleOf(
                STRATEGY_KEY to strategy,
                NAVIGATOR_KEY to navigator
            )
        }
    }
}