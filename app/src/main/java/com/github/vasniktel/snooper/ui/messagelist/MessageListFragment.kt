package com.github.vasniktel.snooper.ui.messagelist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.ui.messagelist.viewmodel.MessageListViewModel
import com.github.vasniktel.snooper.ui.navigators.MessageListNavigator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_message_list.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.qualifier.named

private val TAG = MessageListFragment::class.simpleName

class MessageListFragment : Fragment(), MessageListViewStateCallback, ListItemCallback {
    private lateinit var navigator: MessageListNavigator
    private var user: User? = null

    private lateinit var viewModel: MessageListViewModel
    private lateinit var adapter: MessageListAdapter

    private val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            (messageList.layoutManager as LinearLayoutManager).run {
                // Scroll to top if new data is inserted.
                if (findFirstVisibleItemPosition() == 0) {
                    scrollToPositionWithOffset(0, 0)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            user = it[USER_KEY] as User?
            navigator = it[NAVIGATOR_KEY] as MessageListNavigator
            viewModel = getViewModel(named(it[TYPE_KEY] as MessageListType))
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

        adapter = MessageListAdapter(this).apply {
            registerAdapterDataObserver(dataObserver)
        }
        messageList.apply {
            adapter = this@MessageListFragment.adapter
            setRecyclerListener { holder ->
                (holder as MessageViewHolder).clear()
            }
        }

        refreshLayout.setOnRefreshListener {
            viewModel.onEvent(RefreshEvent(user))
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            Log.d(TAG, "Received action: $it")
            it.applyCallback(this)
        }
    }

    override fun onLoadingState() {
        refreshLayout.isRefreshing = true
    }

    override fun onDataState(data: List<Message>) {
        noDataText.isVisible = data.isEmpty()
        messageList.isVisible = data.isNotEmpty()
        refreshLayout.isRefreshing = false

        adapter.submitList(data)
    }

    override fun onError(message: String, throwable: Throwable?) {
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

    override fun onUserClicked(position: Int, message: Message) {
        findNavController().navigate(navigator.toUserDirection(message.owner))
    }

    override fun onMessageLikeButtonClicked(position: Int, message: Message) {
        viewModel.onEvent(LikeClickedEvent(message))
    }

    override fun onMessageShareButtonClicked(position: Int, message: Message) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "${message.owner.name} has visited ${message.location.address}"
            )
        }

        startActivity(Intent.createChooser(intent, null))
    }

    companion object {
        private const val TYPE_KEY = "messageViewModelKey"
        private const val USER_KEY = "messageUserKey"
        private const val NAVIGATOR_KEY = "messageListNavigatorKey"

        fun create(
            user: User?,
            navigator: MessageListNavigator,
            type: MessageListType
        ) = MessageListFragment().apply {
            arguments = bundleOf(
                USER_KEY to user,
                NAVIGATOR_KEY to navigator,
                TYPE_KEY to type
            )
        }
    }
}