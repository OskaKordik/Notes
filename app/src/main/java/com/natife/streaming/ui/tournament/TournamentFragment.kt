package com.natife.streaming.ui.tournament

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.url
import kotlinx.android.synthetic.main.fragment_tournament.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class TournamentFragment: BaseFragment<TournamentViewModel>() {

    private val adapter : TournamentAdapter by lazy { TournamentAdapter() }
    override fun getLayoutRes() = R.layout.fragment_tournament

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scoreBtn.setOnClickListener {
            viewModel.onScoreClicked()
        }




        subscribe(viewModel.tournament) {
            tournamentTitle.text = it.title
            icon.url(it.icon,it.placeholder)
            addToFavoriteBtn.text = if (it.isFavorite){
                "В избранном"
            }else{//TODO multilang
                "Добавить в избранное"
            }
            addToFavoriteBtn.setOnClickListener {v->
                viewModel.addToFavorite(it)
            }

        }
        tournamentRecycler.layoutManager = GridLayoutManager(context, 4)
        tournamentRecycler.adapter = adapter
        adapter.onBind = {
            viewModel.loadList()
        }

        subscribe(viewModel.list,adapter::submitList)
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(TournamentFragmentArgs.fromBundle(requireArguments()))
    }
}