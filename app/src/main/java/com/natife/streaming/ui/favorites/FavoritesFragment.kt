package com.natife.streaming.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.home.MatchAdapter
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : BaseFragment<FavoriteViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_favorites

    private val matchAdapter: MatchAdapter by lazy {
        MatchAdapter()
    }

    private val favoriteAfapter: FavoritesAdapter by lazy { FavoritesAdapter{
        viewModel.onFavoriteSelected(it)
    } }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesRecycler.layoutManager = LinearLayoutManager(context)
        favoritesRecycler.adapter = favoriteAfapter

        favoritesMatchRecycler.layoutManager = GridLayoutManager(context,3)
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