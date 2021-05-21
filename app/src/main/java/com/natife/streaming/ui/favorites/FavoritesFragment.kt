package com.natife.streaming.ui.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.VerticalGridView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.home.MatchAdapter
import com.natife.streaming.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_home.*

class FavoritesFragment : BaseFragment<FavoriteViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_favorites

    private val matchAdapter: MatchAdapter by lazy {
        MatchAdapter()
    }

    private val favoriteAfapter: FavoritesAdapter by lazy { FavoritesAdapter{
        viewModel.onFavoriteSelected(it)
    } }


    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).logo?.alpha = 1f

        favoritesRecycler.adapter = favoriteAfapter
        favoritesRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM

        favoritesMatchRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM
        favoritesMatchRecycler.setNumColumns(3)
        favoritesMatchRecycler.adapter = matchAdapter


        subscribe(viewModel.defaultLoadingLiveData){
            favoritesProgress.isVisible = it
        }

        matchAdapter.onClick={
            viewModel.goToProfile(it)
        }
        matchAdapter.onBind={
            viewModel.loadNext()
        }
        subscribe(viewModel.favorites,favoriteAfapter::submitList)
        subscribe(viewModel.matches,matchAdapter::submitList)

    }
}