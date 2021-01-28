package com.natife.streaming.ui.matchprofile

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.toDisplayTime
import com.natife.streaming.ui.home.tournament.TournamentDialogArgs
import kotlinx.android.synthetic.main.fragment_match_profile.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class MatchProfileFragment: BaseFragment<MatchProfileViewModel>() {

    private val teamAdapter1: PlayerAdapter by lazy { PlayerAdapter() }
    private val teamAdapter2: PlayerAdapter by lazy { PlayerAdapter() }
    private val episodeAdapter: EpisodeAdapter by lazy { EpisodeAdapter() }

    override fun getLayoutRes(): Int = R.layout.fragment_match_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstTeamRecycler.layoutManager = GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,true)
        firstTeamRecycler.adapter = teamAdapter1
        secondTeamRecycler.layoutManager = GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false)
        secondTeamRecycler.adapter = teamAdapter2
        videoRecycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        videoRecycler.adapter = episodeAdapter


        subscribe(viewModel.info){

            titleInterview.text = it.translates.interviewTranslate
            teamsRecyclerTitle.text = it.translates.playersTranslate

            buttonFullMatch.setText(it.translates.fullGameTranslate)
            //buttonFullMatch.setTime()

            buttonCleanTime.setText(it.translates.ballInPlayTranslate)
            buttonCleanTime.setTime(it.ballInPlayDuration.toDisplayTime())

            buttonReview.setText(it.translates.highlightsTranslate)
            buttonReview.setTime(it.highlightsDuration.toDisplayTime())

            buttonGoals.setText(it.translates.goalsTranslate)
            buttonGoals.setTime(it.goalsDuration.toDisplayTime())

        }
        subscribe(viewModel.title,matchTitle::setText)
        subscribe(viewModel.league,matchLeague::setText)
        subscribe(viewModel.team1,teamAdapter1::submitList)
        subscribe(viewModel.team2,teamAdapter2::submitList)
        subscribe(viewModel.thumbnails,episodeAdapter::submitList)

        buttonBack.setOnClickListener {
            viewModel.back()
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(MatchProfileFragmentArgs.fromBundle(requireArguments()))
    }
}