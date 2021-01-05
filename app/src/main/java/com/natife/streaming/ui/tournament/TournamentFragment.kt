package com.natife.streaming.ui.tournament

import android.os.Bundle
import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_tournament.*

class TournamentFragment: BaseFragment<TournamentViewModel>() {

    override fun getLayoutRes() = R.layout.fragment_tournament

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scoreBtn.setOnClickListener {
            viewModel.onScoreClicked()
        }

        addToFavoriteBtn.setOnClickListener {
            viewModel.addToFavorite()
        }

        subscribe(viewModel.tournament) {

        }
    }
}