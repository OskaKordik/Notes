package com.natife.streaming.ui.popupmatch.statistics

import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router


abstract class PopupStatisticsViewModel : BaseViewModel() {
    abstract fun onStatisticClicked()
    abstract fun onFinishClicked()
}

class PopupStatisticsViewModelImpl(
    private val sport: Int,
    private val matchId: Int,
    private val router: Router,
) : PopupStatisticsViewModel() {

    override fun onStatisticClicked() {
        val direction =
            PopupStatisticsFragmentDirections.actionPopupStatisticsFragmentToPopupVideoFragment(
                sportId = sport,
                matchId = matchId
            )
        router.navigate(direction)
    }

    override fun onFinishClicked() {
        router.navigate(R.id.action_global_nav_main)
    }

}