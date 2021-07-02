package com.natife.streaming.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.KeyEvent
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.leanback.widget.BrowseFrameLayout
import androidx.navigation.NavDestination
import com.natife.streaming.R
import com.natife.streaming.base.BaseActivity
import com.natife.streaming.ext.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity<MainViewModel>() {
    override fun getLayoutRes() = R.layout.activity_main
    override fun getNavHostId() = R.id.globalNavFragment

    private var lastDestination = true


    private var lastClicked = 0L
    private var clickInterval = 200L
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val now = System.currentTimeMillis()
        return if (now - lastClicked > clickInterval) {
            lastClicked = now
            super.onKeyDown(keyCode, event)
        } else {
            true
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Heading in the predominant team color
        mainMotion.predominantColorToGradient("#3560E1")
        viewModel.initialization(resources.getString(R.string.lang))

        mainMenu.setRouter(router)
        mainMenu.activity = this
//        subscribe(viewModel.name) {
        mainMenu.setProfile(resources.getString(R.string.account))
//        }
        subscribe(viewModel.date) {
            if (it != null) {
                data_text.text =
                    it.toDisplay(resources.getString(R.string.lang)).capitalize(Locale.getDefault())
                day_of_weektext_text.text =
                    it.dayOfWeek(resources.getString(R.string.lang)).capitalize(Locale.getDefault())
                year_text.text = if (Date().year(resources.getString(R.string.lang)) != it.year(
                        resources.getString(R.string.lang)
                    )
                )
                    it.year(resources.getString(R.string.lang))
                        .capitalize(Locale.getDefault()) else ""
            }
        }
        subscribe(viewModel.settings) {
            if (it.showScore) {
                score_button.isChecked = false
                score_button.text = resources.getString(R.string.hide_account)
            } else {
                score_button.isChecked = true
                score_button.text = resources.getString(R.string.showe_account)
            }
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
        score_button.setOnClickListener {
            if (!score_button.isChecked) {
                score_button.isChecked = false
                score_button.text = resources.getString(R.string.hide_account)
                viewModel.scoreButtonClicked(false)
            } else {
                score_button.isChecked = true
                score_button.text = resources.getString(R.string.showe_account)
                viewModel.scoreButtonClicked(true)
            }
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
        contentBrowse.onChildFocusListener = object : BrowseFrameLayout.OnChildFocusListener {
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
        mainMotion.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                when (p1) {
                    R.id.start -> {
                        mainMenu.setCloseStyle()
                    }
                    R.id.end -> {
                        mainMenu.setOpenStyle()
                    }
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

        })
        router.addListener { controller, destination, arguments ->
            getLastDestination(destination)
            mainMenu.isVisible = visibilityDesttinationMainMenu(destination)
            topLayout.isVisible = visibilityDesttinationTopLayout(destination)
        }
    }

    private fun getLastDestination(destination: NavDestination) {
        lastDestination = when (destination.id) {
            R.id.searchFragment,
            R.id.favoritesFragment,
            R.id.homeFragment,
            R.id.accountFragment -> true
            else -> false
        }

    }

    private fun visibilityDesttinationMainMenu(destination: NavDestination) =
        when (destination.id) {
            R.id.loginFragment,
            R.id.registerFragment,
            R.id.mypreferencesFragment,
            R.id.action_global_preferences,
            R.id.matchProfileFragment,
            R.id.matchSettingsFragment,
            R.id.watchFragment,
            R.id.playerFragment,
            R.id.calendarFragment,
            R.id.popupVideoFragment,
            R.id.popupStatisticsFragment,
            R.id.billingFragment -> false
            else -> true
        }

    private fun visibilityDesttinationTopLayout(destination: NavDestination) =
        when (destination.id) {
            R.id.loginFragment,
            R.id.registerFragment,
            R.id.mypreferencesFragment,
            R.id.action_global_preferences,
            R.id.matchProfileFragment,
            R.id.matchSettingsFragment,
            R.id.watchFragment,
            R.id.playerFragment,
            R.id.calendarFragment,
            R.id.billingFragment -> false
            else -> true
        }

    override fun onBackPressed() {

        if (lastDestination) {
            val alert = AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.text_close_app_title))
                setMessage(getString(R.string.text_close_app))

                setPositiveButton(getString(R.string.text_yes)) { _, _ ->
                    finish()
                }

                setNegativeButton(getString(R.string.text_no)) { _, _ ->
                }
                setCancelable(true)
            }.create()
            alert.show()
            val buttonPos = alert.getButton(DialogInterface.BUTTON_POSITIVE)
            buttonPos.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    buttonPos.setBackgroundColor(resources.getColor(R.color.gray_400))
                } else {
                    buttonPos.setBackgroundColor(resources.getColor(R.color.white))
                }
            }
            buttonPos.setTextColor(resources.getColor(R.color.black))
            buttonPos.setBackgroundColor(resources.getColor(R.color.white))
            val buttonNeg = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
            buttonNeg.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    buttonNeg.setBackgroundColor(resources.getColor(R.color.gray_400))
                } else {
                    buttonNeg.setBackgroundColor(resources.getColor(R.color.white))
                }
            }
            buttonNeg.requestFocus()
            buttonNeg.setTextColor(resources.getColor(R.color.black))
            buttonNeg.setBackgroundColor(resources.getColor(R.color.gray_400))
        } else super.onBackPressed()

    }




}