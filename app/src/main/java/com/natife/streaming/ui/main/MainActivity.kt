package com.natife.streaming.ui.main

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.leanback.widget.BrowseFrameLayout
import androidx.navigation.NavDestination
import com.natife.streaming.R
import com.natife.streaming.base.BaseActivity
import com.natife.streaming.ext.predominantColorToGradient
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.toDisplay
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>() {

    override fun getLayoutRes() = R.layout.activity_main

    override fun getNavHostId() = R.id.globalNavFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Heading in the predominant team color
        mainMotion.predominantColorToGradient("#3560E1")

        mainMenu.setRouter(router)
        mainMenu.activity = this
        subscribe(viewModel.name) {
            mainMenu.setProfile(it)
        }
        subscribe(viewModel.date) {
            data_text.text = it.toDisplay().toUpperCase()
        }
        data_text.setOnClickListener {
            viewModel.toCalendar()
        }
        day_of_weektext_text.setOnClickListener {
            viewModel.toCalendar()
        }
        calendar_left.setOnClickListener {
            viewModel.previousDay()
        }
        calendar_right.setOnClickListener {
            viewModel.nextDay()
        }
        preferences_button.setOnClickListener {
            viewModel.preferences()
        }


        mainMenu.onChildFocusListener = object : BrowseFrameLayout.OnChildFocusListener {
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
        mainMotion.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                when (p1){
                    R.id.start ->{
                        mainMenu.setCloseStyle()
                    }
                    R.id.end ->{
                        mainMenu.setOpenStyle()
                    }
                }
            }
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

        })

        router.addListener { controller, destination, arguments ->
            //Timber.e("mlkmldkmslkd ${resources.getResourceName(destination.id)} ${destination.id ==  R.id.accountFragment || destination.id ==   R.id.searchFragment} ${backGroup.isVisible }")
//            Timber.tag("TAG").d((destination.id == R.id.mypreferencesFragment || destination.id == R.id.action_global_preferences).toString())
//          backButton.isVisible = destination.id ==  R.id.accountFragment || destination.id == R.id.searchFragment
//            textView3.isVisible = destination.id ==  R.id.accountFragment || destination.id == R.id.searchFragment
            mainMenu.isVisible = visibilityDesttination(destination)
            topLayout.isVisible = visibilityDesttination(destination)
            // Timber.e("mlkmldkmslkd  ${backGroup.isVisible }")
        }

//        backButton.setOnClickListener {
//            viewModel.back()
//        }
    }

    private fun visibilityDesttination(destination: NavDestination) =
        when (destination.id) {
            R.id.loginFragment, R.id.registerFragment, R.id.mypreferencesFragment, R.id.action_global_preferences, R.id.matchProfileFragment, R.id.matchSettingsFragment, R.id.watchFragment, R.id.playerFragment -> false
            else -> true
        }


}