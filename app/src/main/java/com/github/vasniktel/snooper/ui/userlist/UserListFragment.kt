package com.github.vasniktel.snooper.ui.userlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.util.changeVisibility
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_user_list.*
import kotlinx.android.synthetic.main.fragment_user_list.refreshLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

private val TAG = UserListFragment::class.simpleName

class UserListFragment : Fragment(), UserListViewStateCallback {
    private lateinit var strategy: UserListRequestStrategy
    private lateinit var adapter: UserListAdapter
    private var snackBar: Snackbar? = null
    private val viewModel: UserListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        strategy = requireArguments()[STRATEGY_KEY] as UserListRequestStrategy
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
            findNavController().navigate(
                UserListFragmentDirections.actionUserListFragmentToUserFragment(user.id)
            )
        }

        userList.adapter = adapter

        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "Received state: $it")
            it.applyCallback(this)
        })

        viewModel.loadData(strategy)
    }

    override fun onLoadingVisibilityChange(visible: Boolean) {
        refreshLayout.isRefreshing = visible
    }

    override fun onDataLoaded(data: List<User>) {
        changeVisibility(noUserText, data.isEmpty())
        changeVisibility(userList, data.isNotEmpty())
        adapter.submitList(data)
        snackBar?.dismiss()
    }

    override fun onError(message: String, throwable: Throwable?) {
        Log.e(TAG, "An error occurred: '$message'", throwable)
        snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
        snackBar?.show()
    }

    companion object {
        private const val STRATEGY_KEY = "userListRequestStrategy"

        fun create(strategy: UserListRequestStrategy) =
            UserListFragment().apply {
                arguments = bundleOf(
                    STRATEGY_KEY to strategy
                )
            }
    }
}