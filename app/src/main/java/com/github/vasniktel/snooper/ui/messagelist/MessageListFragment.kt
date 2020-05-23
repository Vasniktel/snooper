package com.github.vasniktel.snooper.ui.messagelist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.ui.feed.FeedFragmentDirections
import com.github.vasniktel.snooper.ui.user.UserFragment
import com.github.vasniktel.snooper.util.changeVisibility
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_message_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private val TAG = MessageListFragment::class.simpleName

class MessageListFragment : Fragment(), MessageListViewStateCallback, ListItemCallback {
    private lateinit var strategy: MessageRequestStrategy
    private val viewModel: MessageListViewModel by viewModel()
    private lateinit var adapter: MessageListAdapter
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            strategy = it[STRATEGY_KEY] as MessageRequestStrategy
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActvityCreated: ${::adapter.isInitialized}")

        adapter = MessageListAdapter(this)
        messageList.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchNewData(strategy)
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            Log.d(TAG, "Received action: $it")
            it.applyCallback(this)
        }
    }

    override fun onLoadingTopVisibilityChange(visible: Boolean) {
        refreshLayout.isRefreshing = visible
    }

    override fun onLoadingBottomVisibilityChange(visible: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onNextPageLoaded(page: List<Message>) {
        val data = adapter.currentList.toMutableList()
        data += page
        adapter.submitList(data)
    }

    override fun onNewDataLoaded(data: List<Message>) {
        changeVisibility(noDataText, data.isEmpty())
        changeVisibility(messageList, data.isNotEmpty())
        adapter.submitList(data)
        snackBar?.dismiss()
    }

    override fun onError(message: String, throwable: Throwable?) {
        Log.e(TAG, "An error occurred: '$message'", throwable)
        snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
        snackBar?.show()
    }

    override fun onPopulateState() {
        viewModel.getData(strategy)
    }

    override fun onUserClicked(position: Int, message: Message) {
        findNavController().navigate(
            R.id.userFragment,
            UserFragment.makeArgs(message.ownerId)
        )
    }

    override fun onMessageMenuButtonClicked(position: Int, message: Message) {
        TODO("Not yet implemented")
    }

    override fun onMessageLikeButtonClicked(position: Int, message: Message) {
        message.copy(likes = message.likes + 1).let {
            viewModel.addLike(it)
            val data = adapter.currentList.toMutableList()
            data[position] = it
            adapter.submitList(data)
        }
    }

    override fun onMessageShareButtonClicked(position: Int, message: Message) {
        startActivity(
            Intent.createChooser(
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "${message.ownerName} has visited ${message.latitude} ${message.longitude}"
                    )
                    type = "text/plain"
                },
                null
            )
        )
    }

    override fun onMapClicked(position: Int, message: Message) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val STRATEGY_KEY = "messageRequestStrategy"

        fun create(
            strategy: MessageRequestStrategy
        ) = MessageListFragment().apply {
            arguments = bundleOf(
                STRATEGY_KEY to strategy
            )
        }
    }
}