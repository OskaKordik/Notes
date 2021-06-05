package com.natife.streaming.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.VerticalGridView
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_home

//    val adapter: MatchAdapter by lazy { MatchAdapter ()}


    //
//    private fun applyAlpha(alpha: Float){
////        (activity as MainActivity).logo?.alpha = alpha
////        buttonLive?.alpha = 1 - alpha
////        buttonLock?.alpha = 1 - alpha
////        buttonScore?.alpha = 1 - alpha
////        buttonSport?.alpha = 1 - alpha
////        buttonTourney?.alpha = 1 - alpha
////        dateRight?.alpha = 1 - alpha
////        dateLeft?.alpha = 1 - alpha
////        dateText?.alpha = 1 - alpha
//    }
//
//    private val transitionListener = object :
//        MotionLayout.TransitionListener {
//        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
//        }
//
//        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
//            applyAlpha(p3)
//        }
//
//        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
//        }
//
//        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
//        }
//
//    }
//
//    override fun onResume() {
//        ((activity as MainActivity).mainMotion as MotionLayout).addTransitionListener(transitionListener)
//        super.onResume()
//    }
    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        applyAlpha(((activity as MainActivity).mainMotion as MotionLayout).progress)


//        subscribe(viewModel.subOnly){//+
//            buttonLock.setImageResource(if (it){R.drawable.button_lock_red}else{R.drawable.button_lock})
//        }

//        subscribe(viewModel.date){//+
//            dateText.text = it.toDisplay().toUpperCase()
//        }

//        adapter.onClick = {
//            viewModel.toMatchProfile(it)
//        }

//        buttonScore.setOnClickListener {//+
//            viewModel.showScoreDialog()
//        }
//        buttonSport.setOnClickListener {//+
//            viewModel.showSportDialog()
//        }
//        buttonTourney.setOnClickListener {//+
//            viewModel.showTourneyDialog(adapter.currentList)
//        }
//        buttonLive.setOnClickListener {//+
//            viewModel.showLiveDialog()
//        }
//        buttonLock.setOnClickListener {//+
//            viewModel.subOnlyChange()
//        }


//        dateText.setOnClickListener {//+
//            viewModel.toCalendar()
//        }
//        dateLeft.setOnClickListener {//+
//            viewModel.previousDay()
//        }
//        dateRight.setOnClickListener {//+
//            viewModel.nextDay()
//        }

        val adapter = TournamentAdapter(viewModel)
        homeRecycler.windowAlignment = VerticalGridView.WINDOW_ALIGN_BOTH_EDGE;

        homeRecycler.isFocusable = false
        homeRecycler.adapter = adapter
        homeRecycler.setNumColumns(1)
        homeRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM


        adapter.onBind = {
            val a = adapter.itemCount
            viewModel.loadList()
        }
        subscribe(viewModel.list) {
            viewModel.setListTournament(it)
        }
        subscribe(viewModel.listTournament) {

            adapter.submitList(it)
            //if (FOCUSE_FIRST_TIME && !it.isEmpty()){
            //  homeRecycler.requestFocus()
            //  FOCUSE_FIRST_TIME = false
            //}

        }

    }

//    override fun onPause() {
//
//        ((activity as MainActivity).mainMotion as MotionLayout).removeTransitionListener(transitionListener)
//        super.onPause()
//    }

}