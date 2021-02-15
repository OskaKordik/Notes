package com.natife.streaming.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : BaseFragment<FavoriteViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_favorites
    private val favoriteAfapter: FavoritesAdapter by lazy { FavoritesAdapter(){
        viewModel.onFavoriteSelected(it)
    } }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesRecycler.layoutManager = LinearLayoutManager(context)
        favoritesRecycler.adapter = favoriteAfapter

        subscribe(viewModel.favorites,favoriteAfapter::submitList)

    }
}