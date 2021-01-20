package com.natife.streaming.ui.home

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class HomeFragment : BaseFragment<HomeViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_home

    val adapter: MatchAdapter by lazy { MatchAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ((activity as MainActivity).mainMotion as MotionLayout).setTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                Timber.e("p1 $p1 p2 $p2 p3 $p3")
                (activity as MainActivity).logo.alpha = p3
                buttonLive.alpha = 1 - p3
                buttonLock.alpha = 1 - p3
                buttonScore.alpha = 1 - p3
                buttonSport.alpha = 1 - p3
                buttonTourney.alpha = 1 - p3
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

        })

        buttonScore.setOnClickListener {
            viewModel.showScoreDialog()
        }
        buttonSport.setOnClickListener {
            viewModel.showSportDialog()
        }
        buttonTourney.setOnClickListener {
            viewModel.showTourneyDialog(adapter.currentList)
        }
        buttonLive.setOnClickListener {
            viewModel.showLiveDialog()
        }

        homeRecycler.layoutManager = GridLayoutManager(context, 4)
        homeRecycler.adapter = adapter

        adapter.onBind = {
            viewModel.loadList()
        }

        subscribe(viewModel.list) {
            Timber.e("submit")
            adapter.submitList(it)
        }

    }
}