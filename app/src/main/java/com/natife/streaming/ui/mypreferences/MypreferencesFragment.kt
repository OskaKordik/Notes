package com.natife.streaming.ui.mypreferences

import android.os.Bundle
import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.predominantColorToGradient
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_mypreferences_new.*


class MypreferencesFragment : BaseFragment<MypreferencesViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_mypreferences_new
    private val sportAdapter by lazy {
        SportsAdapter(viewModel::kindOfSportClicked)
    }
    private val tournamentAdapter by lazy {
        TournamentAdapter(viewModel::listOfTournamentsClicked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Heading in the predominant team color
        topConstraintLayout.predominantColorToGradient("#CB312A")
        viewModel.initialization()
        kindsOfSportsRecyclerView.adapter = sportAdapter
        listOfTournamentsRecyclerView.adapter = tournamentAdapter

        subscribe(viewModel.sportsList) {
            sportAdapter.submitList(it)
        }
        subscribe(viewModel.tournament) {
            tournamentAdapter.submitList(it.zipWithNext())
        }
    }
}


