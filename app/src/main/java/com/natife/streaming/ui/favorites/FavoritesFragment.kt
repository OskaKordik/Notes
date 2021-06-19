package com.natife.streaming.ui.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.VerticalGridView
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.home.MatchAdapter
import kotlinx.android.synthetic.main.fragment_favorites_new.*
import kotlinx.android.synthetic.main.fragment_home.*

class FavoritesFragment : BaseFragment<FavoriteViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_favorites_new

    private val matchAdapter: MatchAdapter by lazy {
        MatchAdapter()
    }

    private val favoriteAdapter: FavoritesAdapter by lazy {
        FavoritesAdapter {
            viewModel.onFavoriteSelected(it)
        }
    }


    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialization()
        favoritesRecycler.apply {
            this.requestFocus()
            isSelected = true
        }

        favoritesRecycler.adapter = favoriteAdapter
        favoritesRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM
        favoritesRecycler.windowAlignment = VerticalGridView.WINDOW_ALIGN_BOTH_EDGE

        favoritesMatchRecycler.windowAlignment = VerticalGridView.WINDOW_ALIGN_BOTH_EDGE
        favoritesMatchRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM
        favoritesMatchRecycler.setNumColumns(3)
        favoritesMatchRecycler.adapter = matchAdapter


        subscribe(viewModel.defaultLoadingLiveData) {
            favoritesProgress.isVisible = it
        }

        matchAdapter.onClick = {
            viewModel.goToProfile(it)
        }
        matchAdapter.onBind = {
            viewModel.loadNext()
        }
        subscribe(viewModel.favorites, favoriteAdapter::submitList)
        subscribe(viewModel.matches, matchAdapter::submitList)

    }
}