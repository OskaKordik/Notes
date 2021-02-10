package com.natife.streaming.ui.matchprofile

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.toDisplayTime
import kotlinx.android.synthetic.main.fragment_match_profile.*
import kotlinx.android.synthetic.main.fragment_match_profile.buttonBack
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class MatchProfileFragment: BaseFragment<MatchProfileViewModel>() {

    private val teamAdapter1: PlayerAdapter by lazy { PlayerAdapter() }
    private val teamAdapter2: PlayerAdapter by lazy { PlayerAdapter() }
    private val episodeAdapter: EpisodeAdapter by lazy { EpisodeAdapter() }

    override fun getLayoutRes(): Int = R.layout.fragment_match_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstTeamRecycler.layoutManager = GridLayoutManager(context,5,GridLayoutManager.VERTICAL,false)
        firstTeamRecycler.adapter = teamAdapter1
        secondTeamRecycler.layoutManager = GridLayoutManager(context,5,GridLayoutManager.VERTICAL,false)
        secondTeamRecycler.adapter = teamAdapter2
        videoRecycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        videoRecycler.adapter = episodeAdapter
        episodeAdapter.onClick={
            viewModel.play(it)
        }


        subscribe(viewModel.info){

            titleInterview.text = it.translates.interviewTranslate
            teamsRecyclerTitle.text = it.translates.playersTranslate

            buttonFullMatch.setText(it.translates.fullGameTranslate)
            buttonFullMatch.setText(it.translates.fullGameTranslate)

            buttonCleanTime.setText(it.translates.ballInPlayTranslate)
            buttonCleanTime.setTime(it.ballInPlayDuration.toDisplayTime())

            buttonReview.setText(it.translates.highlightsTranslate)
            buttonReview.setTime(it.highlightsDuration.toDisplayTime())

            buttonGoals.setText(it.translates.goalsTranslate)
            buttonGoals.setTime(it.goalsDuration.toDisplayTime())

        }
        subscribe(viewModel.fullVideoDuration){
            buttonFullMatch.setTime(it.toDisplayTime())
        }
        subscribe(viewModel.title,matchTitle::setText)
        subscribe(viewModel.league,matchLeague::setText)
        subscribe(viewModel.team1,teamAdapter1::submitList)
        subscribe(viewModel.team2,teamAdapter2::submitList)
        subscribe(viewModel.episodes,episodeAdapter::submitList)

        teamAdapter1.onClick ={
            viewModel.player(it)
        }
        teamAdapter2.onClick ={
            viewModel.player(it)
        }

        buttonBack.setOnClickListener {
            viewModel.back()
        }
        buttonSettings.setOnClickListener {
            viewModel.goToSettings()
        }
        buttonGoals.setOnClickListener {
            viewModel.goals()
        }
        buttonReview.setOnClickListener {
            viewModel.review()
        }
        buttonFullMatch.setOnClickListener {
            viewModel.fullMatch()
        }
        buttonCleanTime.setOnClickListener {
            viewModel.ballInPlay()
        }
    }


    override fun getParameters(): ParametersDefinition = {
        parametersOf(MatchProfileFragmentArgs.fromBundle(requireArguments()))
    }
}