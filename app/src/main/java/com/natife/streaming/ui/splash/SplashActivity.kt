package com.natife.streaming.ui.splash

import android.os.Bundle
import com.natife.streaming.base.BaseActivity
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.ext.startClearActivity
import com.natife.streaming.router.Router
import com.natife.streaming.ui.main.MainActivity

class SplashActivity: BaseActivity<EmptyViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startClearActivity(MainActivity::class)
    }

    override fun getLayoutRes(): Int? {
        return null
    }

    override fun onCreateRouter(): Router? {
       return null
    }
}