package com.natife.streaming.ui.main

import android.os.Bundle
import android.view.KeyEvent
import androidx.constraintlayout.motion.widget.MotionLayout
import com.natife.streaming.R
import com.natife.streaming.base.BaseActivity
import com.natife.streaming.router.Router
import kotlinx.android.synthetic.main.activity_main.mainMenu
import kotlinx.android.synthetic.main.activity_main.mainMotion

class MainActivity : BaseActivity<MainViewModel>() {

    override fun getLayoutRes() = R.layout.activity_main

    override fun onCreateRouter(): Router? {
        return Router(this, R.id.mainNavFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainMenu.setRouter(router)


    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {

        if (mainMenu.hasFocus()) {
            (mainMotion as MotionLayout).transitionToEnd()
        } else {
            (mainMotion as MotionLayout).transitionToStart()
        }
        return super.dispatchKeyEvent(event)

    }
}