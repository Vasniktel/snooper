package com.github.vasniktel.snooper.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.ui.messagelist.FeedRequestStrategy
import com.github.vasniktel.snooper.ui.messagelist.MessageListFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedFragment : Fragment() {
    private val viewModel: FeedFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.apply {
            if (findFragmentById(R.id.messageListFragment) == null) {
                beginTransaction()
                    .add(
                        R.id.messageListFragment,
                        MessageListFragment.create(
                            FeedRequestStrategy(viewModel.currentUser.id)
                        )
                    )
                    .commit()
            }
        }
    }
}