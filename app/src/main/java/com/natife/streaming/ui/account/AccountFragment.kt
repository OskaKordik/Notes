package com.natife.streaming.ui.account

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseFragment<AccountViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).logo.isVisible = true

        buttonLogout.setOnClickListener {
            viewModel.logout()
        }


        subscribe(viewModel.nameLiveData, name::setText)
        subscribe(viewModel.emailLiveData, email::setText)

    }
}