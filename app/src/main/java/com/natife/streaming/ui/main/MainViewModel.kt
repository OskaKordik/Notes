package com.natife.streaming.ui.main

import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.router.Router

class MainViewModel(
    private val authPrefs: AuthPrefs,
    private val router: Router
) : BaseViewModel() {

    init {
        launch {
            val isLoggedIn = authPrefs.isLoggedIn()
            if (isLoggedIn) {
                router.navigate(R.id.action_global_nav_auth)
            } else {
                router.navigate(R.id.action_global_nav_auth)
            }
        }
    }
}