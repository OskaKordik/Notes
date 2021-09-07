package com.natife.streaming.ui.live

import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetLiveVideoUseCase

abstract class LiveViewModel : BaseViewModel() {
    abstract fun onFinishClicked()
}

class LiveViewModelImpl(
    private val matchId: Int,
    private val sportId: Int,
    private val title: String,
    private val router: Router,
    private val getLiveVideoUseCase: GetLiveVideoUseCase
) : LiveViewModel() {
    override fun onFinishClicked() {
        router.navigate(R.id.action_global_nav_main)
    }

    init {
        launch {
            getLiveVideoUseCase.execute(matchId, sportId)
        }
    }
}