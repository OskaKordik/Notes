package com.natife.streaming.ui.tournament

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.leanback.widget.BaseGridView
import androidx.recyclerview.widget.GridLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.url
import com.natife.streaming.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_tournament.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class TournamentFragment: BaseFragment<TournamentViewModel>() {

    private val adapter : TournamentAdapter by lazy { TournamentAdapter(){
        viewModel.toProfile(it)
    } }
    override fun getLayoutRes() = R.layout.fragment_tournament

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).logo?.alpha = 0f

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

        subscribe(viewModel.team){
            tournamentTitle.text = it.name
            icon.url(it.image,it.placeholder)
            addToFavoriteBtn.text = if (it.isFavorite){
                "В избранном"
            }else{//TODO multilang
                "Добавить в избранное"
            }
            addToFavoriteBtn.setOnClickListener {v->
                viewModel.addToFavorite(it)
            }
        }
        subscribe(viewModel.player){
            tournamentTitle.text = it.name
            icon.url(it.image,it.imagePlaceholder)
            addToFavoriteBtn.text = if (it.isFavorite){
                "В избранном"
            }else{//TODO multilang
                "Добавить в избранное"
            }
            addToFavoriteBtn.setOnClickListener {v->
                viewModel.addToFavorite(it)
            }
        }

        tournamentRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM
        tournamentRecycler.setNumColumns(4)
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