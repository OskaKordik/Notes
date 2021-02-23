package com.natife.streaming.ui.main

import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.leanback.widget.BrowseFrameLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.natife.streaming.R
import com.natife.streaming.base.BaseActivity
import com.natife.streaming.ext.dp
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_player.*
import timber.log.Timber
import java.lang.Exception

class MainActivity : BaseActivity<MainViewModel>() {

    override fun getLayoutRes() = R.layout.activity_main

    override fun getNavHostId() = R.id.globalNavFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainMenu.setRouter(router)
        mainMenu.activity = this
        subscribe(viewModel.name){
            mainMenu.setProfile(it)
        }

        mainMenu.onChildFocusListener = object : BrowseFrameLayout.OnChildFocusListener{
            override fun onRequestFocusInDescendants(
                direction: Int,
                previouslyFocusedRect: Rect?
            ): Boolean {
                return false
            }

            override fun onRequestChildFocus(child: View?, focused: View?) {
                (mainMotion as MotionLayout).transitionToEnd()
            }

        }
        contentBrowse.onChildFocusListener = object : BrowseFrameLayout.OnChildFocusListener{
            override fun onRequestFocusInDescendants(
                direction: Int,
                previouslyFocusedRect: Rect?
            ): Boolean {
                return false
            }

            override fun onRequestChildFocus(child: View?, focused: View?) {
                (mainMotion as MotionLayout).transitionToStart()
            }

        }

        router.addListener { controller, destination, arguments ->
            //Timber.e("mlkmldkmslkd ${resources.getResourceName(destination.id)} ${destination.id ==  R.id.accountFragment || destination.id ==   R.id.searchFragment} ${backGroup.isVisible }")
          backButton.isVisible = destination.id ==  R.id.accountFragment || destination.id == R.id.searchFragment
            textView3.isVisible = destination.id ==  R.id.accountFragment || destination.id == R.id.searchFragment

          // Timber.e("mlkmldkmslkd  ${backGroup.isVisible }")
        }

        backButton.setOnClickListener {
            viewModel.back()
        }
    }




}