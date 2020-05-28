package com.github.vasniktel.snooper.ui.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.search.SearchQueryProducer
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

private val TAB_NAME = arrayOf("POSTS", "USERS")

class SearchFragment : Fragment() {
    private val provider: SearchQueryProducer = get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchViewPager.adapter = SearchFragmentTabsAdapter(this)
        TabLayoutMediator(searchTabLayout, searchViewPager) { tab, position ->
            tab.text = TAB_NAME[position]
        }.attach()

        searchEditBox.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                provider.produce(searchEditBox.text.toString())
                true
            } else {
                false
            }
        }
    }
}