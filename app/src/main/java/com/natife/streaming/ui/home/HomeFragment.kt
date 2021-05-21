package com.natife.streaming.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.doOnLayout
import androidx.leanback.widget.BaseGridView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.toDisplay
import com.natife.streaming.router.Router
import com.natife.streaming.ui.main.MainActivity
import com.natife.streaming.ui.tournament.TournamentFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class HomeFragment : BaseFragment<HomeViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_home

    val adapter: MatchAdapter by lazy { MatchAdapter ()}


    private fun applyAlpha(alpha: Float){
//        (activity as MainActivity).logo?.alpha = alpha
        buttonLive?.alpha = 1 - alpha
        buttonLock?.alpha = 1 - alpha
        buttonScore?.alpha = 1 - alpha
        buttonSport?.alpha = 1 - alpha
        buttonTourney?.alpha = 1 - alpha
        dateRight?.alpha = 1 - alpha
        dateLeft?.alpha = 1 - alpha
        dateText?.alpha = 1 - alpha
    }

    private val transitionListener = object :
        MotionLayout.TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            applyAlpha(p3)
        }

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
        }

    }

    override fun onResume() {
        ((activity as MainActivity).mainMotion as MotionLayout).addTransitionListener(transitionListener)
        super.onResume()
    }
    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyAlpha(((activity as MainActivity).mainMotion as MotionLayout).progress)



        subscribe(viewModel.subOnly){
            buttonLock.setImageResource(if (it){R.drawable.button_lock_red}else{R.drawable.button_lock})
        }

        subscribe(viewModel.date){
            dateText.text = it.toDisplay().toUpperCase()
        }

        adapter.onClick={
            viewModel.toMatchProfile(it)
        }

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
        buttonLock.setOnClickListener {
            viewModel.subOnlyChange()
        }
        dateText.setOnClickListener {
            viewModel.toCalendar()
        }
        dateLeft.setOnClickListener {
            viewModel.previousDay()
        }
        dateRight.setOnClickListener {
            viewModel.nextDay()
        }



        homeRecycler.isFocusable = false

        homeRecycler.adapter = adapter
        homeRecycler.setNumColumns(4)
        homeRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM


        adapter.onBind = {
            viewModel.loadList()
        }

        subscribe(viewModel.list) {

            adapter.submitList(it)
            //if (FOCUSE_FIRST_TIME && !it.isEmpty()){
              //  homeRecycler.requestFocus()
              //  FOCUSE_FIRST_TIME = false
            //}

        }

    }

    override fun onPause() {

        ((activity as MainActivity).mainMotion as MotionLayout).removeTransitionListener(transitionListener)
        super.onPause()
    }

}