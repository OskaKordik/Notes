package com.natife.streaming.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.VerticalGridView
import androidx.navigation.navGraphViewModels
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_search_result.*

class SearchResultFragment : BaseFragment<EmptyViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_search_result
    private val searchResultViewModel: SearchResultViewModel by navGraphViewModels(R.id.nav_main)
    private var typeOfSearch: SearchResult.Type? = null

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            typeOfSearch = when (it.getInt(TYPE_OF_SEARCH)) {
                0 -> SearchResult.Type.PLAYER
                1 -> SearchResult.Type.TEAM
                2 -> SearchResult.Type.TOURNAMENT
                else -> null
            }
        }
        val adapter: SearchAdapter by lazy { SearchAdapter(searchResultViewModel, typeOfSearch) }
        tab_search_recycler.windowAlignment = VerticalGridView.WINDOW_ALIGN_BOTH_EDGE
        tab_search_recycler.isFocusable = false
        tab_search_recycler.setNumColumns(3)
        tab_search_recycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM
        tab_search_recycler.adapter = adapter
        adapter.onClick = {
            searchResultViewModel.searchResultClicked(it)
        }

        subscribe(searchResultViewModel.resultsPlayer) {
            if (typeOfSearch == SearchResult.Type.PLAYER) adapter.submitList(it)
        }

        subscribe(searchResultViewModel.resultsTeam) {
            if (typeOfSearch == SearchResult.Type.TEAM) adapter.submitList(it)
        }

        subscribe(searchResultViewModel.resultsTournament) {
            if (typeOfSearch == SearchResult.Type.TOURNAMENT) adapter.submitList(it)
        }
    }

    companion object {
        private const val TYPE_OF_SEARCH = "TYPE_OF_SEARCH"

        @JvmStatic
        fun newInstance(type: Int) = SearchResultFragment().apply {
            arguments = Bundle().apply {
                putInt(TYPE_OF_SEARCH, type)
            }
        }
    }

}