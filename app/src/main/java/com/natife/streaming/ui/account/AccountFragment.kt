package com.natife.streaming.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.predominantColorToGradient
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseFragment<AccountViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_account


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).logo.isVisible = true


        requireActivity().findViewById<Group>(R.id.main_group).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.main_background_group).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.search_background_group).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.profile_background_group).visibility = View.VISIBLE
        //Heading in the predominant team color
        requireActivity().findViewById<MotionLayout>(R.id.mainMotion).predominantColorToGradient("#3560E1")

        buttonLogout.setOnClickListener {
            viewModel.logout()
        }

        subscribeLiveData()
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeLiveData(){
        viewModel.profileLiveData.observe(viewLifecycleOwner, {profile ->
            requireActivity().findViewById<TextView>(R.id.profile_name).text = "${profile.lastName} ${profile.firstName}"
            text_phone.text = "${getString(R.string.text_phone)} ${profile.phone}"
            text_country.text = "${getString(R.string.text_country)} ${profile.country?.nameEng}"
            email.text = "${getString(R.string.text_email)} ${profile.email}"
        })
    }


}