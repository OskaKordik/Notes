package com.natife.streaming.ui.billing

import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router

abstract class BillingViewModel : BaseViewModel() {
    abstract fun onFinishClicked()
}

class BillingViewModelImpl(
    private val sport: Int,
    private val matchId: Int,
    private val router: Router,
) : BillingViewModel() {
    override fun onFinishClicked() {
        router.navigate(R.id.action_global_nav_main)
    }
}