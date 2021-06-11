package com.natife.streaming.ui.popupmatch.video.watch

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.toDisplayTime
import com.natife.streaming.ui.popupmatch.PopupSharedViewModel
import kotlinx.android.synthetic.main.fragment_match_profile.*


class TabWatchFragment : BaseFragment<EmptyViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_tab_watch
    private val popupSharedViewModel: PopupSharedViewModel by navGraphViewModels(R.id.popupVideo)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe(popupSharedViewModel.matchInfo) {
            buttonFullMatch.setText(it.translates.fullGameTranslate)

            buttonCleanTime.setText(it.translates.ballInPlayTranslate)
            buttonCleanTime.setTime(it.ballInPlayDuration.toDisplayTime())

            buttonReview.setText(it.translates.highlightsTranslate)
            buttonReview.setTime(it.highlightsDuration.toDisplayTime())

            buttonGoals.setText(it.translates.goalsTranslate)
            buttonGoals.setTime(it.goalsDuration.toDisplayTime())
        }
        subscribe(popupSharedViewModel.fullVideoDuration) {
            buttonFullMatch.setTime(it.toDisplayTime())
        }

//        buttonGoals.setOnClickListener {
//            viewModel.goals()
//        }
//        buttonReview.setOnClickListener {
//            viewModel.review()
//        }
//        buttonFullMatch.setOnClickListener {
//            viewModel.fullMatch()
//        }
//        buttonCleanTime.setOnClickListener {
//            viewModel.ballInPlay()
//        }
    }
}