package com.natife.streaming.ui.subscriptions

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.Group
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.predominantColorToGradient
import kotlinx.android.synthetic.main.fragment_account.*

class SubscriptionsFragment : BaseFragment<SubscriptionsViewModel>() {
    override fun getLayoutRes() = R.layout.subscriptions_fragment

    override fun onStart(){
        super.onStart()

        requireActivity().findViewById<Group>(R.id.main_group).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.main_background_group).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.search_background_group).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.profile_background_group).visibility = View.GONE
        requireActivity().findViewById<com.natife.streaming.custom.SideMenu>(R.id.mainMenu).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.profile_subscriptions_background_group).visibility = View.VISIBLE
        requireActivity().findViewById<MotionLayout>(R.id.mainMotion).predominantColorToGradient("#CB312A")
    }


    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<Group>(R.id.profile_background_group).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.profile_subscriptions_background_group).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.main_group).visibility = View.VISIBLE
        requireActivity().findViewById<com.natife.streaming.custom.SideMenu>(R.id.mainMenu).visibility = View.VISIBLE
        requireActivity().findViewById<Group>(R.id.main_background_group).visibility = View.VISIBLE
        requireActivity().findViewById<MotionLayout>(R.id.mainMotion).predominantColorToGradient("#3560E1")
    }

}