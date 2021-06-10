package com.natife.streaming.ui.popupmatch.statistics

import com.natife.streaming.base.BaseViewModel

abstract class PopupStatisticsViewModel : BaseViewModel() {

}

class PopupStatisticsViewModelImpl(
    private val sport: Int,
    private val matchId: Int,
) : PopupStatisticsViewModel() {

}