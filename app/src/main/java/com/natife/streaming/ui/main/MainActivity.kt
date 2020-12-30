package com.natife.streaming.ui.main

import android.os.Bundle
import com.natife.streaming.R
import com.natife.streaming.base.BaseActivity
import com.natife.streaming.router.Router

class MainActivity : BaseActivity<MainViewModel>() {

    override fun getLayoutRes() = R.layout.activity_main

    override fun onCreateRouter(): Router? {
        return Router(this, R.id.mainNavFragment)
    }
}